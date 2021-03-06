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
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.ArticleTypeEnums;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.StudentTaskStatusEnums;
import com.server.management_system.enums.TemplateEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.StudentTaskArticleVo;
import com.server.management_system.vo.StudentTaskWriteRecordVo;
import com.server.management_system.vo.StudentVo;
import com.server.management_system.vo.TeacherTaskReleaseVo;
import com.server.management_system.vo.req.StudentReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-28
 */
@Service
public class StudentService {
    @Resource
    private StudentInfoRepository studentInfoRepository;
    @Resource
    private TeacherClassRelationRepository teacherClassRelationRepository;
    @Resource
    private TeacherReleaseRecordRepository teacherReleaseRecordRepository;
    @Resource
    private ArticleInfoRepository articleInfoRepository;
    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
    private StudentTaskArticleRepository studentTaskArticleRepository;
    @Resource
    private StudentGradeRecordRepository studentGradeRecordRepository;
    @Autowired
    private AdminService adminService;

    public StudentVo getStudentInfo(Long studentId) {
        StudentInfo studentInfo = studentInfoRepository.selectByStudentId(studentId);
        if (studentInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该学生不存在");
        }
        return adminService.getStudentVo(studentInfo);
    }

    public Map<String, Object> updateStudentInfo(StudentReq studentReq) {
        StudentInfo studentInfo = studentInfoRepository.selectByStudentId(studentReq.getStudentId());
        if (studentInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该学生不存在");
        }
        studentInfo.setStudentNumber(studentReq.getStudentNumber());
        studentInfo.setClassId(studentReq.getClassId());
        if (!studentInfo.getClassId().equals(studentReq.getClassId())) {
            List<StudentTaskArticle> studentTaskArticleList =
                    studentTaskArticleRepository.selectByStudentId(studentInfo.getStudentId());
            if (!CollectionUtils.isEmpty(studentTaskArticleList)) {
                studentTaskArticleList.forEach(studentTaskArticle -> {
                    studentTaskArticle.setDeleted(DeleteStatusEnums.DELETED.getCode());
                    studentTaskArticleRepository.updateById(studentTaskArticle);
                });
            }
            List<TeacherReleaseRecord> teacherReleaseRecordList =
                    teacherReleaseRecordRepository.selectByClassIds(Lists.newArrayList(studentInfo.getClassId()));
            if (!CollectionUtils.isEmpty(teacherReleaseRecordList)) {
                for (TeacherReleaseRecord teacherReleaseRecord : teacherReleaseRecordList) {
                    ArticleInfo articleInfo =
                            articleInfoRepository.selectByArticleId(teacherReleaseRecord.getArticleId());
                    if (articleInfo == null) {
                        continue;
                    }
                    StudentTaskArticle studentTaskArticle = new StudentTaskArticle();
                    studentTaskArticle.setArticleId(articleInfo.getId());
                    studentTaskArticle.setClassId(studentInfo.getClassId());
                    studentTaskArticle.setStudentId(studentInfo.getStudentId());
                    studentTaskArticle.setTeacherId(teacherReleaseRecord.getTeacherId());
                    studentTaskArticle.setUpdateTime(System.currentTimeMillis());
                    studentTaskArticle.setCreateTime(System.currentTimeMillis());
                    studentTaskArticle.setDeleted(DeleteStatusEnums.DELETED.getCode());
                    studentTaskArticle.setStatus(StudentTaskStatusEnums.UNKNOWN.getCode());
                    studentTaskArticle.setRemark(StringUtils.EMPTY);
                    if (articleInfo.getType().equals(ArticleTypeEnums.STUDENT.getCode())) {
                        studentTaskArticleRepository.insert(studentTaskArticle);
                    }
                }
            }
            List<StudentGradeRecord> studentGradeRecordList =
                    studentGradeRecordRepository.selectByStudentId(studentInfo.getStudentId());
            if (!CollectionUtils.isEmpty(studentGradeRecordList)) {
                studentGradeRecordList.forEach(studentGradeRecord -> {
                    studentGradeRecord.setDeleted(DeleteStatusEnums.DELETED.getCode());
                    studentGradeRecordRepository.updateById(studentGradeRecord);
                });
            }
        }
        studentInfo.setName(studentReq.getStudentName());
        studentInfo.setNativePlace(studentReq.getNativePlace());
        studentInfo.setNowPlace(studentReq.getNowPlace());
        studentInfo.setSex(studentReq.getSex());
        studentInfoRepository.updateById(studentInfo);
        return Maps.newHashMap();
    }

    public RestListData<TeacherTaskReleaseVo> getTeacherTaskRecord(Long studentId) {
        List<TeacherTaskReleaseVo> teacherTaskReleaseVoList = Lists.newArrayList();
        StudentInfo studentInfo = studentInfoRepository.selectByStudentId(studentId);
        if (studentInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该学生不存在");
        }
        List<TeacherClassRelation> teacherClassRelationList =
                teacherClassRelationRepository.selectByClassId(studentInfo.getClassId());
        if (CollectionUtils.isEmpty(teacherClassRelationList)) {
            return RestListData.create(teacherTaskReleaseVoList.size(), teacherTaskReleaseVoList);
        }
        for (TeacherClassRelation teacherClassRelation : teacherClassRelationList) {
            List<TeacherReleaseRecord> teacherReleaseRecordList =
                    teacherReleaseRecordRepository.selectByTeacherId(teacherClassRelation.getTeacherId());
            for (TeacherReleaseRecord teacherReleaseRecord : teacherReleaseRecordList) {
                TeacherTaskReleaseVo teacherTaskReleaseVo = new TeacherTaskReleaseVo();
                teacherTaskReleaseVo.setArticleId(teacherReleaseRecord.getArticleId());
                teacherTaskReleaseVo.setRecordId(teacherReleaseRecord.getId());
                ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(teacherReleaseRecord.getArticleId());
                if (articleInfo != null) {
                    teacherTaskReleaseVo.setArticleName(articleInfo.getName());
                }
                teacherTaskReleaseVo.setTeacherId(teacherReleaseRecord.getTeacherId());
                teacherTaskReleaseVo.setReleaseTime(teacherReleaseRecord.getReleaseTime());
                UserInfo userInfo = userInfoRepository.selectByUserId(teacherReleaseRecord.getTeacherId());
                if (userInfo != null) {
                    teacherTaskReleaseVo.setTeacherName(userInfo.getName());
                }
                if (articleInfo != null && articleInfo.getType().equals(ArticleTypeEnums.TASK.getCode())) {
                    teacherTaskReleaseVoList.add(teacherTaskReleaseVo);
                }
            }
        }
        return RestListData.create(teacherTaskReleaseVoList.size(), teacherTaskReleaseVoList);
    }

    public RestListData<StudentTaskArticleVo> getTaskList(Long studentId) {
        List<StudentTaskArticleVo> studentTaskArticleVoList = Lists.newArrayList();
        List<StudentTaskArticle> studentTaskArticleList = studentTaskArticleRepository.selectByStudentId(studentId);
        if (CollectionUtils.isEmpty(studentTaskArticleList)) {
            return RestListData.create(studentTaskArticleVoList.size(), studentTaskArticleVoList);
        }
        studentTaskArticleList.forEach(studentTaskArticle -> {
            StudentTaskArticleVo studentTaskArticleVo = new StudentTaskArticleVo();
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(studentTaskArticle.getArticleId());
            if (articleInfo != null) {
                studentTaskArticleVo.setTaskId(studentTaskArticle.getId());
                studentTaskArticleVo.setArticleId(articleInfo.getId());
                studentTaskArticleVo.setArticleName(articleInfo.getName());
                studentTaskArticleVo.setCreateTime(articleInfo.getStartTime());
                studentTaskArticleVo.setEndTime(articleInfo.getEndTime());
                studentTaskArticleVoList.add(studentTaskArticleVo);
            }
        });
        return RestListData.create(studentTaskArticleVoList.size(), studentTaskArticleVoList);
    }

    public RestListData<StudentTaskWriteRecordVo> getTaskWriteRecordList(Long studentId) {
        List<StudentTaskWriteRecordVo> studentTaskWriteRecordVoList = Lists.newArrayList();
        List<StudentTaskArticle> studentTaskArticleList =
                studentTaskArticleRepository.selectStatusByStudentId(studentId);
        if (CollectionUtils.isEmpty(studentTaskArticleList)) {
            return RestListData.create(studentTaskWriteRecordVoList.size(), studentTaskWriteRecordVoList);
        }
        studentTaskArticleList.forEach(studentTaskArticle -> {
            StudentTaskWriteRecordVo studentTaskWriteRecordVo = new StudentTaskWriteRecordVo();
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(studentTaskArticle.getArticleId());
            if (articleInfo != null) {
                studentTaskWriteRecordVo.setArticleId(articleInfo.getId());
                studentTaskWriteRecordVo.setArticleName(articleInfo.getName());
                studentTaskWriteRecordVo.setRecordId(studentTaskArticle.getId());
                studentTaskWriteRecordVo.setReleaseTime(studentTaskArticle.getUpdateTime());
                studentTaskWriteRecordVo.setStatus(studentTaskArticle.getStatus());
                studentTaskWriteRecordVoList.add(studentTaskWriteRecordVo);
            }
        });
        return RestListData.create(studentTaskWriteRecordVoList.size(), studentTaskWriteRecordVoList);
    }

    public Map<String, Object> uploadTeacherTask(Long taskId, Long studentId, MultipartFile file) {
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
            articleInfo.setType(ArticleTypeEnums.STUDENT.getCode());
            articleInfo.setTemplate(TemplateEnums.NO.getCode());
            articleInfo.setStartTime(System.currentTimeMillis());
            articleInfo.setEndTime(System.currentTimeMillis());
            articleInfoRepository.insert(articleInfo);
            StudentTaskArticle studentTaskArticle = studentTaskArticleRepository.selectByTaskId(taskId);
            studentTaskArticle.setStatus(StudentTaskStatusEnums.NOT_MARK.getCode());
            studentTaskArticle.setArticleId(articleInfo.getId());
            studentTaskArticleRepository.updateById(studentTaskArticle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Maps.newHashMap();
    }
}
