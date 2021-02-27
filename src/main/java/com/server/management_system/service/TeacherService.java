package com.server.management_system.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.ArticleInfoRepository;
import com.server.management_system.dao.StudentInfoRepository;
import com.server.management_system.dao.StudentTaskArticleRepository;
import com.server.management_system.dao.TeacherClassRelationRepository;
import com.server.management_system.dao.TeacherReleaseRecordRepository;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.ArticleInfo;
import com.server.management_system.domain.StudentInfo;
import com.server.management_system.domain.StudentTaskArticle;
import com.server.management_system.domain.TeacherClassRelation;
import com.server.management_system.domain.TeacherReleaseRecord;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.StudentTaskStatusEnums;
import com.server.management_system.enums.TemplateEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
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
    private UserInfoRepository userInfoRepository;

    public RestListData<TeacherTaskArticleVo> getTeacherTaskArticleList(PageRequestParam pageRequestParam,
            String search) {
        List<TeacherTaskArticleVo> teacherTaskArticleVoList = Lists.newArrayList();
        List<ArticleInfo> articleInfoList = articleInfoRepository.selectTaskByTemplate(TemplateEnums.YES.getCode());
        if (CollectionUtils.isEmpty(articleInfoList)) {
            return RestListData.create(teacherTaskArticleVoList.size(), teacherTaskArticleVoList);
        }
        articleInfoList.forEach(articleInfo -> {
            TeacherTaskArticleVo teacherTaskArticleVo = new TeacherTaskArticleVo();
            teacherTaskArticleVo.setArticleId(articleInfo.getId());
            teacherTaskArticleVo.setArticleName(articleInfo.getName());
            teacherTaskArticleVo.setStartTime(articleInfo.getStartTime());
            teacherTaskArticleVo.setEndTime(articleInfo.getEndTime());
            if (StringUtils.containsIgnoreCase(teacherTaskArticleVo.getArticleName(), search)) {
                teacherTaskArticleVoList.add(teacherTaskArticleVo);
            }
        });
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), teacherTaskArticleVoList.size());
        return RestListData.create(teacherTaskArticleVoList.size(), teacherTaskArticleVoList.subList(start, end));

    }

    public RestRsp<Map<String, Object>> teacherTaskRelease(Long teacherId, MultipartFile file) {
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
            articleInfo.setPath("/src/main/resources/file/teacher" + teacherId.toString() + "/" + fileName);
            articleInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
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
                        studentTaskArticle.setDeleted(DeleteStatusEnums.DELETED.getCode());
                        studentTaskArticle.setStatus(StudentTaskStatusEnums.UNKNOWN.getCode());
                        studentTaskArticle.setRemark(StringUtils.EMPTY);
                        studentTaskArticleRepository.insert(studentTaskArticle);
                    });
                }
            }
            return RestRsp.success(objectMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestRsp.fail(ErrorCode.SERVER_ERROR, "任务发布失败");
    }
}
