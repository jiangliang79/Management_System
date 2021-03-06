package com.server.management_system.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.ArticleInfoRepository;
import com.server.management_system.dao.StudentGradeRecordRepository;
import com.server.management_system.dao.StudentInfoRepository;
import com.server.management_system.dao.StudentTaskArticleRepository;
import com.server.management_system.dao.TeacherClassRelationRepository;
import com.server.management_system.dao.TeacherReleaseRecordRepository;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.ArticleInfo;
import com.server.management_system.domain.StudentGradeRecord;
import com.server.management_system.domain.StudentInfo;
import com.server.management_system.domain.StudentTaskArticle;
import com.server.management_system.domain.TeacherClassRelation;
import com.server.management_system.domain.TeacherReleaseRecord;
import com.server.management_system.enums.ArticleTypeEnums;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.StudentTaskStatusEnums;
import com.server.management_system.enums.TeacherTaskArticleTypeEnums;
import com.server.management_system.enums.TemplateEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.StudentTaskArticleVo;
import com.server.management_system.vo.TeacherTaskArticleVo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-27
 */
@Service
public class TeacherService {

    @Resource
    private ArticleInfoRepository articleInfoRepository;
    @Resource
    private StudentTaskArticleRepository studentTaskArticleRepository;
    @Resource
    private TeacherReleaseRecordRepository teacherReleaseRecordRepository;
    @Resource
    private TeacherClassRelationRepository teacherClassRelationRepository;
    @Resource
    private StudentInfoRepository studentInfoRepository;
    @Resource
    private StudentGradeRecordRepository studentGradeRecordRepository;
    @Resource
    private UserInfoRepository userInfoRepository;
    @Autowired
    private AdminService adminService;

    public RestListData<TeacherTaskArticleVo> getTeacherTaskArticleList(PageRequestParam pageRequestParam,
            String search, Integer type) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<TeacherTaskArticleVo> teacherTaskArticleVoList = Lists.newArrayList();
        List<ArticleInfo> articleInfoList;
        if (type.equals(TeacherTaskArticleTypeEnums.GRADE.getCode())) {
            articleInfoList = articleInfoRepository.selectTaskByTemplateAndGrade(TemplateEnums.YES.getCode());
        } else {
            articleInfoList = articleInfoRepository.selectTaskByTemplateAndNotGrade(TemplateEnums.YES.getCode());
        }
        if (CollectionUtils.isEmpty(articleInfoList)) {
            return RestListData.create(teacherTaskArticleVoList.size(), teacherTaskArticleVoList);
        }
        String finalSearch = search;
        articleInfoList.forEach(articleInfo -> {
            TeacherTaskArticleVo teacherTaskArticleVo = new TeacherTaskArticleVo();
            teacherTaskArticleVo.setArticleId(articleInfo.getId());
            teacherTaskArticleVo.setArticleName(articleInfo.getName());
            teacherTaskArticleVo.setStartTime(articleInfo.getStartTime());
            teacherTaskArticleVo.setEndTime(articleInfo.getEndTime());
            if (StringUtils.containsIgnoreCase(teacherTaskArticleVo.getArticleName(), finalSearch)) {
                teacherTaskArticleVoList.add(teacherTaskArticleVo);
            }
        });
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), teacherTaskArticleVoList.size());
        return RestListData.create(teacherTaskArticleVoList.size(), teacherTaskArticleVoList.subList(start, end));

    }

    public RestRsp<Map<String, Object>> teacherTaskRelease(Long teacherId, MultipartFile file, Integer type) {
        Map<String, Object> objectMap = Maps.newHashMap();
        try {
            if (file.isEmpty()) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件名不能为空");
            }
            // 设置文件存储路径
            File path = new File(System.getProperty("user.dir") + "/src/main/resources/file/teacher");
            File upload = new File(path.getAbsolutePath(), "/" + teacherId.toString());
            File dest = new File(upload.getAbsolutePath() + "/" + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setName(fileName);
            articleInfo.setPath("/src/main/resources/file/teacher/" + teacherId.toString() + "/" + fileName);
            articleInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            articleInfo.setType(type);
            articleInfo.setTemplate(TemplateEnums.NO.getCode());
            articleInfo.setStartTime(System.currentTimeMillis());
            articleInfo.setEndTime(System.currentTimeMillis());
            articleInfoRepository.insert(articleInfo);
            List<TeacherClassRelation> teacherClassRelationList =
                    teacherClassRelationRepository.selectByTeacherId(teacherId);
            if (!CollectionUtils.isEmpty(teacherClassRelationList)) {
                List<Long> classIds = Lists.newArrayList();
                teacherClassRelationList.forEach(teacherClassRelation -> {
                    classIds.add(teacherClassRelation.getClassId());
                });
                classIds.forEach(classId -> {
                    TeacherReleaseRecord teacherReleaseRecord = new TeacherReleaseRecord();
                    teacherReleaseRecord.setArticleId(articleInfo.getId());
                    teacherReleaseRecord.setTeacherId(teacherId);
                    teacherReleaseRecord.setClassId(classId);
                    teacherReleaseRecord.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
                    teacherReleaseRecord.setReleaseTime(System.currentTimeMillis());
                    teacherReleaseRecordRepository.insert(teacherReleaseRecord);
                });
                List<StudentInfo> studentInfoList = studentInfoRepository.selectStudentListByClassIds(classIds);
                if (!CollectionUtils.isEmpty(studentInfoList)) {
                    studentInfoList.forEach(studentInfo -> {
                        StudentTaskArticle studentTaskArticle = new StudentTaskArticle();
                        studentTaskArticle.setArticleId(articleInfo.getId());
                        studentTaskArticle.setClassId(studentInfo.getClassId());
                        studentTaskArticle.setStudentId(studentInfo.getStudentId());
                        studentTaskArticle.setTeacherId(teacherId);
                        studentTaskArticle.setCreateTime(System.currentTimeMillis());
                        studentTaskArticle.setUpdateTime(System.currentTimeMillis());
                        studentTaskArticle.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
                        studentTaskArticle.setStatus(StudentTaskStatusEnums.UNKNOWN.getCode());
                        studentTaskArticle.setRemark(StringUtils.EMPTY);
                        if (articleInfo.getType().equals(ArticleTypeEnums.STUDENT.getCode())) {
                            studentTaskArticleRepository.insert(studentTaskArticle);
                        }
                    });
                }
            }
            return RestRsp.success(objectMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestRsp.fail(ErrorCode.SERVER_ERROR, "任务发布失败");
    }

    public Map<String, Object> checkTaskArticle(Long taskId, Integer status, String remark) {
        StudentTaskArticle studentTaskArticle = studentTaskArticleRepository.selectByTaskId(taskId);
        if (studentTaskArticle == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该任务不存在");
        }
        if (status == 0) {
            studentTaskArticle.setStatus(StudentTaskStatusEnums.SUCCESS.getCode());
        } else {
            studentTaskArticle.setStatus(StudentTaskStatusEnums.FAIL.getCode());
        }
        if (StringUtils.isNotEmpty(remark)) {
            studentTaskArticle.setRemark(remark);
        }
        studentTaskArticleRepository.updateById(studentTaskArticle);
        return Maps.newHashMap();
    }

    public RestRsp<Map<String, Object>> uploadStudentGrade(Long teacherId, Long studentId, MultipartFile file) {
        try {
            ArticleInfo articleInfo = new ArticleInfo();
            if (file.isEmpty()) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件名不能为空");
            }
            // 设置文件存储路径
            File path = new File(System.getProperty("user.dir") + "/src/main/resources/file/student");
            File upload = new File(path.getAbsolutePath(), "/" + studentId.toString());
            File dest = new File(upload.getAbsolutePath() + "/" + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            articleInfo.setName(fileName);
            articleInfo.setPath("/src/main/resources/file/student/" + studentId.toString() + "/" + fileName);
            articleInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            articleInfo.setType(ArticleTypeEnums.TEACHER.getCode());
            articleInfo.setTemplate(TemplateEnums.NO.getCode());
            articleInfo.setStartTime(System.currentTimeMillis());
            articleInfo.setEndTime(System.currentTimeMillis());
            articleInfoRepository.insert(articleInfo);
            StudentInfo studentInfo = studentInfoRepository.selectByStudentId(studentId);
            if (studentInfo != null) {
                StudentGradeRecord studentGradeRecord = new StudentGradeRecord();
                studentGradeRecord.setArticleId(articleInfo.getId());
                studentGradeRecord.setStudentId(studentId);
                studentGradeRecord.setTeacherId(teacherId);
                studentGradeRecord.setClassId(studentInfo.getClassId());
                studentGradeRecord.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
                studentGradeRecord.setReleaseTime(System.currentTimeMillis());
                studentGradeRecordRepository.insert(studentGradeRecord);
            }
            return RestRsp.success(Maps.newHashMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RestRsp.fail(ErrorCode.SERVER_ERROR, "成绩评定失败");
    }

    public RestListData<StudentTaskArticleVo> getTeacherTaskArticleCheckList(Long teacherId,
            PageRequestParam pageRequestParam, String search) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<StudentTaskArticleVo> studentTaskArticleVoList = Lists.newArrayList();
        List<StudentTaskArticle> studentTaskArticleList =
                studentTaskArticleRepository.selectNotMarkListByTeacherId(teacherId);
        if (CollectionUtils.isEmpty(studentTaskArticleList)) {
            return RestListData.create(studentTaskArticleVoList.size(), studentTaskArticleVoList);
        }
        for (StudentTaskArticle studentTaskArticle : studentTaskArticleList) {
            StudentTaskArticleVo studentTaskArticleVo = adminService.getStudentTaskArticleVo(studentTaskArticle);
            if (StringUtils.containsIgnoreCase(studentTaskArticleVo.getArticleName(), search) || StringUtils
                    .containsIgnoreCase(studentTaskArticleVo.getStudentName(), search)) {
                studentTaskArticleVoList.add(studentTaskArticleVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), studentTaskArticleVoList.size());
        return RestListData.create(studentTaskArticleVoList.size(), studentTaskArticleVoList.subList(start, end));
    }
}
