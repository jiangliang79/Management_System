package com.server.management_system.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;


import org.jodconverter.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.ArticleInfoRepository;
import com.server.management_system.dao.ClassInfoRepository;
import com.server.management_system.dao.CollegeInfoRepository;
import com.server.management_system.dao.ProfessionInfoRepository;
import com.server.management_system.dao.StudentAttendanceRepository;
import com.server.management_system.dao.StudentGradeRecordRepository;
import com.server.management_system.dao.StudentGradeRepository;
import com.server.management_system.dao.StudentInfoRepository;
import com.server.management_system.dao.StudentTaskArticleRepository;
import com.server.management_system.dao.TeacherClassRelationRepository;
import com.server.management_system.dao.TeacherReleaseRecordRepository;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.ArticleInfo;
import com.server.management_system.domain.ClassInfo;
import com.server.management_system.domain.CollegeInfo;
import com.server.management_system.domain.ProfessionInfo;
import com.server.management_system.domain.StudentAttendance;
import com.server.management_system.domain.StudentGrade;
import com.server.management_system.domain.StudentGradeRecord;
import com.server.management_system.domain.StudentInfo;
import com.server.management_system.domain.StudentTaskArticle;
import com.server.management_system.domain.TeacherClassRelation;
import com.server.management_system.domain.TeacherReleaseRecord;
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.DeleteOrganizationEnums;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.RecordTypeEnums;
import com.server.management_system.enums.StudentTaskStatusEnums;
import com.server.management_system.enums.TemplateEnums;
import com.server.management_system.enums.UserTypeEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.vo.ArticleVo;
import com.server.management_system.vo.ClassVo;
import com.server.management_system.vo.CollegeVo;
import com.server.management_system.vo.ProfessionVo;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.StudentGradeRecordVo;
import com.server.management_system.vo.StudentTaskArticleVo;
import com.server.management_system.vo.StudentVo;
import com.server.management_system.vo.TeacherClassVo;
import com.server.management_system.vo.TeacherTaskReleaseVo;
import com.server.management_system.vo.TeacherVo;
import com.server.management_system.vo.UserVo;
import com.server.management_system.vo.req.AddClassReq;
import com.server.management_system.vo.req.AddProfessionReq;
import com.server.management_system.vo.req.AddUserReq;
import com.server.management_system.vo.req.DeleteOrganizationReq;
import com.server.management_system.vo.req.EditArticleReq;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Service
@Slf4j
public class AdminService {
    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
    private StudentInfoRepository studentInfoRepository;
    @Resource
    private CollegeInfoRepository collegeInfoRepository;
    @Resource
    private ProfessionInfoRepository professionInfoRepository;
    @Resource
    private ClassInfoRepository classInfoRepository;
    @Resource
    private TeacherClassRelationRepository teacherClassRelationRepository;
    @Resource
    private StudentTaskArticleRepository studentTaskArticleRepository;
    @Resource
    private ArticleInfoRepository articleInfoRepository;
    @Resource
    private TeacherReleaseRecordRepository teacherReleaseRecordRepository;
    @Autowired
    private DocumentConverter converter;
    @Resource
    private StudentGradeRecordRepository studentGradeRecordRepository;
    @Resource
    private StudentGradeRepository studentGradeRepository;
    @Resource
    private StudentAttendanceRepository studentAttendanceRepository;

    public Map<String, Object> addUser(AddUserReq addUserReq) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(addUserReq.getUsername());
        userInfo.setPassword(addUserReq.getPassword());
        userInfo.setName(addUserReq.getName());
        userInfo.setType(addUserReq.getUserType());
        userInfo.setDescription(addUserReq.getDescription());
        userInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setUpdateTime(System.currentTimeMillis());
        UserInfo oldUserInfo = userInfoRepository.selectByUserName(addUserReq.getUsername());
        if (oldUserInfo != null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户已经存在");
        }
        userInfoRepository.insert(userInfo);
        if (addUserReq.getUserType().equals(UserTypeEnums.STUDENT.getCode())) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStudentId(userInfo.getId());
            studentInfo.setName(userInfo.getName());
            studentInfo.setPhone(userInfo.getPhone());
            studentInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            studentInfo.setCreateTime(System.currentTimeMillis());
            studentInfo.setUpdateTime(System.currentTimeMillis());
            studentInfoRepository.insert(studentInfo);
        } else if (addUserReq.getUserType().equals(UserTypeEnums.COLLEGE.getCode())) {
            CollegeInfo collegeInfo = new CollegeInfo();
            collegeInfo.setCollegeId(userInfo.getId());
            collegeInfo.setName(userInfo.getName());
            collegeInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            collegeInfo.setCreateTime(System.currentTimeMillis());
            collegeInfo.setUpdateTime(System.currentTimeMillis());
            collegeInfo.setDescription(userInfo.getDescription());
            collegeInfoRepository.insert(collegeInfo);
        }
        return Maps.newHashMap();
    }

    public Map<String, Object> editUser(AddUserReq addUserReq) {
        UserInfo userInfo = userInfoRepository.selectByUserName(addUserReq.getUsername());
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "用户不存在");
        }
        userInfo.setUsername(addUserReq.getUsername());
        if (!addUserReq.getUserType().equals(userInfo.getType()) && userInfo.getType()
                .equals(UserTypeEnums.STUDENT.getCode())) {
            StudentInfo studentInfo = studentInfoRepository.selectByStudentId(userInfo.getId());
            studentInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
            studentInfoRepository.updateById(studentInfo);
        } else if (!addUserReq.getUserType().equals(userInfo.getType()) && userInfo.getType()
                .equals(UserTypeEnums.COLLEGE.getCode())) {
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(userInfo.getId());
            collegeInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
            collegeInfoRepository.updateById(collegeInfo);
        }
        if (!addUserReq.getUserType().equals(userInfo.getType()) && addUserReq.getUserType()
                .equals(UserTypeEnums.STUDENT.getCode())) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setName(userInfo.getName());
            studentInfo.setStudentId(userInfo.getId());
            studentInfo.setPhone(userInfo.getPhone());
            studentInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            studentInfo.setCreateTime(System.currentTimeMillis());
            studentInfo.setUpdateTime(System.currentTimeMillis());
            studentInfoRepository.insert(studentInfo);
        } else if (!addUserReq.getUserType().equals(userInfo.getType()) && addUserReq.getUserType()
                .equals(UserTypeEnums.COLLEGE.getCode())) {
            CollegeInfo collegeInfo = new CollegeInfo();
            collegeInfo.setName(userInfo.getName());
            collegeInfo.setCollegeId(userInfo.getId());
            collegeInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            collegeInfo.setCreateTime(System.currentTimeMillis());
            collegeInfo.setUpdateTime(System.currentTimeMillis());
            collegeInfo.setDescription(userInfo.getDescription());
            collegeInfoRepository.insert(collegeInfo);
        }
        userInfo.setType(addUserReq.getUserType());
        userInfo.setName(addUserReq.getName());
        userInfo.setDescription(addUserReq.getDescription());
        userInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setUpdateTime(System.currentTimeMillis());
        userInfoRepository.updateById(userInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> resetPassword(Long userId, String password) {
        UserInfo userInfo = userInfoRepository.selectByUserId(userId);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户不存在");
        }
        if (StringUtils.isEmpty(password)) {
            userInfo.setPassword(userInfo.getUsername());
        } else {
            userInfo.setPassword(password);
        }
        userInfoRepository.updateById(userInfo);
        return Maps.newHashMap();
    }

    public RestListData<UserVo> getUserList(PageRequestParam pageRequestParam, String search) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<UserVo> userVoList = Lists.newArrayList();
        List<UserInfo> userInfoList = userInfoRepository.selectUserList();
        if (CollectionUtils.isEmpty(userInfoList)) {
            return RestListData.create(userVoList.size(), userVoList);
        }
        for (UserInfo userInfo : userInfoList) {
            if (StringUtils.containsIgnoreCase(userInfo.getUsername(), search) || StringUtils
                    .containsIgnoreCase(userInfo.getName(), search)) {
                UserVo userVo = new UserVo();
                userVo.setUserId(userInfo.getId());
                userVo.setUsername(userInfo.getUsername());
                userVo.setName(userInfo.getName());
                userVo.setUserType(userInfo.getType());
                userVo.setCreateTime(userInfo.getCreateTime());
                userVo.setDescription(userInfo.getDescription());
                userVoList.add(userVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), userVoList.size());
        return RestListData.create(userVoList.size(), userVoList.subList(start, end));
    }

    public Map<String, Object> deleteUser(Long userId, Integer type) {
        UserInfo userInfo = userInfoRepository.selectByUserId(userId);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户不存在");
        }
        userInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
        if (type.equals(UserTypeEnums.STUDENT.getCode())) {
            StudentInfo studentInfo = studentInfoRepository.selectByStudentId(userId);
            if (studentInfo != null) {
                studentInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
                studentInfoRepository.updateById(studentInfo);
            }
        }
        if (type.equals(UserTypeEnums.COLLEGE.getCode())) {
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(userId);
            if (collegeInfo != null) {
                collegeInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
                collegeInfoRepository.updateById(collegeInfo);
            }
        }
        userInfoRepository.updateById(userInfo);
        return Maps.newHashMap();
    }

    public RestListData<CollegeVo> getCollegeList(PageRequestParam pageRequestParam, String search,
            boolean isAll) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<CollegeVo> collegeVoList = Lists.newArrayList();
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.getCollegeList();
        if (CollectionUtils.isEmpty(collegeInfoList)) {
            return RestListData.create(collegeVoList.size(), collegeVoList);
        }
        for (CollegeInfo collegeInfo : collegeInfoList) {
            CollegeVo collegeVo = new CollegeVo();
            collegeVo.setCollegeId(collegeInfo.getCollegeId());
            collegeVo.setCollegeName(collegeInfo.getName());
            collegeVo.setCreateTime(collegeInfo.getCreateTime());
            collegeVo.setDescription(collegeInfo.getDescription());
            if (StringUtils.containsIgnoreCase(collegeVo.getCollegeName(), search)) {
                collegeVoList.add(collegeVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), collegeVoList.size());
        if (isAll) {
            return RestListData.create(collegeVoList.size(), collegeVoList);
        } else {
            return RestListData.create(collegeVoList.size(), collegeVoList.subList(start, end));
        }
    }

    public RestListData<ProfessionVo> getProfessionList(PageRequestParam pageRequestParam, String search,
            Long collegeId) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<ProfessionVo> professionVoList = Lists.newArrayList();
        List<ProfessionInfo> professionInfoList;
        if (collegeId == null) {
            professionInfoList = professionInfoRepository.getProfessionList();
        } else {
            professionInfoList = professionInfoRepository.selectByCollegeId(collegeId);
        }
        if (CollectionUtils.isEmpty(professionInfoList)) {
            return RestListData.create(professionVoList.size(), professionVoList);
        }
        for (ProfessionInfo professionInfo : professionInfoList) {
            ProfessionVo professionVo = new ProfessionVo();
            professionVo.setProfessionId(professionInfo.getId());
            professionVo.setProfessionName(professionInfo.getName());
            professionVo.setCollegeId(professionInfo.getCollegeId());
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(professionVo.getCollegeId());
            professionVo.setCollegeName(collegeInfo.getName());
            professionVo.setCreateTime(System.currentTimeMillis());
            professionVo.setUpdateTime(System.currentTimeMillis());
            professionVo.setDescription(professionInfo.getDescription());
            if (StringUtils.containsIgnoreCase(professionVo.getCollegeName(), search) || StringUtils
                    .containsIgnoreCase(professionVo.getProfessionName(), search)) {
                professionVoList.add(professionVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), professionVoList.size());
        return RestListData.create(professionVoList.size(), professionVoList.subList(start, end));
    }

    public RestListData<ClassVo> getClassList(PageRequestParam pageRequestParam, String search,
            Long professionId) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<ClassVo> classVoList = Lists.newArrayList();
        List<ClassInfo> classInfoList;
        if (professionId == null) {
            classInfoList = classInfoRepository.getClassList();
        } else {
            classInfoList = classInfoRepository.selectByProfessionId(professionId);
        }
        if (CollectionUtils.isEmpty(classInfoList)) {
            return RestListData.create(classVoList.size(), classVoList);
        }
        for (ClassInfo classInfo : classInfoList) {
            ClassVo classVo = new ClassVo();
            classVo.setClassId(classInfo.getId());
            classVo.setClassName(classInfo.getName());
            classVo.setProfessionId(classInfo.getProfessionId());
            classVo.setCollegeId(classInfo.getCollegeId());
            classVo.setDescription(classInfo.getDescription());
            classVo.setCreateTime(System.currentTimeMillis());
            classVo.setUpdateTime(System.currentTimeMillis());
            ProfessionInfo professionInfo = professionInfoRepository.selectByProfessionId(classVo.getProfessionId());
            if (professionInfo != null) {
                classVo.setProfessionName(professionInfo.getName());
            }
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(classVo.getCollegeId());
            if (collegeInfo != null) {
                classVo.setCollegeName(collegeInfo.getName());
            }
            if (StringUtils.containsIgnoreCase(classVo.getCollegeName(), search) || StringUtils
                    .containsIgnoreCase(classVo.getProfessionName(), search) || StringUtils
                    .containsIgnoreCase(classVo.getClassName(), search)) {
                classVoList.add(classVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), classVoList.size());
        return RestListData.create(classVoList.size(), classVoList.subList(start, end));
    }

    public Map<String, Object> addClass(AddClassReq addClassReq) {
        if (addClassReq.getProfessionId() == null || addClassReq.getCollegeId() == null || StringUtils
                .isEmpty(addClassReq.getClassName())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        ClassInfo classInfo = classInfoRepository
                .selectByNameAndProfessionIdAndCollegeId(addClassReq.getClassName(), addClassReq.getCollegeId(),
                        addClassReq.getProfessionId());
        if (classInfo != null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "该班级已经存在");
        }
        classInfo = new ClassInfo();
        classInfo.setName(addClassReq.getClassName());
        classInfo.setProfessionId(addClassReq.getProfessionId());
        classInfo.setCollegeId(addClassReq.getCollegeId());
        classInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        classInfo.setCreateTime(System.currentTimeMillis());
        classInfo.setUpdateTime(System.currentTimeMillis());
        classInfo.setDescription(StringUtils.EMPTY);
        classInfoRepository.insert(classInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> editClass(AddClassReq addClassReq) {
        if (addClassReq.getProfessionId() == null || addClassReq.getClassId() == null
                || addClassReq.getCollegeId() == null || StringUtils.isEmpty(addClassReq.getClassName())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        ClassInfo classInfo = classInfoRepository.selectByClassId(addClassReq.getClassId());
        if (classInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该班级不存在");
        }
        classInfo.setName(addClassReq.getClassName());
        classInfo.setCollegeId(addClassReq.getCollegeId());
        classInfo.setProfessionId(addClassReq.getProfessionId());
        classInfoRepository.updateById(classInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> addProfession(AddProfessionReq addProfessionReq) {
        if (StringUtils.isEmpty(addProfessionReq.getProfessionName())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        ProfessionInfo professionInfo = professionInfoRepository
                .selectByCollegeIdAndName(addProfessionReq.getCollegeId(), addProfessionReq.getProfessionName());
        if (professionInfo != null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "该专业已经存在");
        }
        professionInfo = new ProfessionInfo();
        professionInfo.setCollegeId(addProfessionReq.getCollegeId());
        professionInfo.setName(addProfessionReq.getProfessionName());
        professionInfo.setCreateTime(System.currentTimeMillis());
        professionInfo.setUpdateTime(System.currentTimeMillis());
        professionInfo.setDescription(addProfessionReq.getDescription());
        professionInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        professionInfoRepository.insert(professionInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> editProfession(AddProfessionReq addProfessionReq) {
        if (StringUtils.isEmpty(addProfessionReq.getProfessionName()) || addProfessionReq.getProfessionId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        ProfessionInfo professionInfo =
                professionInfoRepository.selectByProfessionId(addProfessionReq.getProfessionId());
        if (professionInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "该专业不存在");
        }
        professionInfo.setCollegeId(addProfessionReq.getCollegeId());
        professionInfo.setName(addProfessionReq.getProfessionName());
        professionInfo.setUpdateTime(System.currentTimeMillis());
        professionInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        professionInfo.setDescription(addProfessionReq.getDescription());
        professionInfoRepository.updateById(professionInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> deleteOrganization(DeleteOrganizationReq deleteOrganizationReq) {
        if (deleteOrganizationReq.getType().equals(DeleteOrganizationEnums.COLLEGE.getCode())) {
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(deleteOrganizationReq.getId());
            if (collegeInfo == null) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "该学院不存在");
            }
            collegeInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
            collegeInfoRepository.updateById(collegeInfo);
            UserInfo userInfo = userInfoRepository.selectByUserId(deleteOrganizationReq.getId());
            if (userInfo != null) {
                userInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
                userInfoRepository.updateById(userInfo);
            }
        } else if (deleteOrganizationReq.getType().equals(DeleteOrganizationEnums.PROFESSION.getCode())) {
            ProfessionInfo professionInfo =
                    professionInfoRepository.selectByProfessionId(deleteOrganizationReq.getId());
            if (professionInfo == null) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "该专业不存在");
            }
            professionInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
            professionInfoRepository.updateById(professionInfo);
        } else {
            ClassInfo classInfo = classInfoRepository.selectByClassId(deleteOrganizationReq.getId());
            if (classInfo == null) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "该专业不存在");
            }
            classInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
            classInfoRepository.updateById(classInfo);
        }
        return Maps.newHashMap();
    }

    public RestListData<TeacherVo> getTeacherList(PageRequestParam pageRequestParam, String search) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<TeacherVo> teacherVoList = Lists.newArrayList();
        List<UserInfo> userInfoList = userInfoRepository.selectUserByType(UserTypeEnums.TEACHER.getCode());
        if (CollectionUtils.isEmpty(userInfoList)) {
            return RestListData.create(teacherVoList.size(), teacherVoList);
        }
        for (UserInfo userInfo : userInfoList) {
            TeacherVo teacherVo = new TeacherVo();
            teacherVo.setTeacherId(userInfo.getId());
            teacherVo.setTeacherName(userInfo.getName());
            teacherVo.setTeacherAccountNumber(userInfo.getUsername());
            teacherVo.setPhone(userInfo.getPhone());
            if (StringUtils.containsIgnoreCase(teacherVo.getTeacherName(), search) || StringUtils
                    .containsIgnoreCase(teacherVo.getTeacherAccountNumber(), search)) {
                teacherVoList.add(teacherVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), teacherVoList.size());
        return RestListData.create(teacherVoList.size(), teacherVoList.subList(start, end));
    }

    public RestListData<TeacherClassVo> getTeacherClassList(PageRequestParam pageRequestParam, String search) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<TeacherClassVo> teacherClassVoList = Lists.newArrayList();
        List<TeacherClassRelation> teacherClassRelationList = teacherClassRelationRepository.selectAll();
        if (CollectionUtils.isEmpty(teacherClassRelationList)) {
            return RestListData.create(teacherClassVoList.size(), teacherClassVoList);
        }
        for (TeacherClassRelation teacherClassRelation : teacherClassRelationList) {
            TeacherClassVo teacherClassVo = new TeacherClassVo();
            UserInfo userInfo = userInfoRepository.selectByUserId(teacherClassRelation.getTeacherId());
            teacherClassVo.setRecordId(teacherClassRelation.getId());
            teacherClassVo.setTeacherId(userInfo.getId());
            teacherClassVo.setTeacherName(userInfo.getName());
            teacherClassVo.setTeacherAccountNumber(userInfo.getUsername());
            teacherClassVo.setPhone(userInfo.getPhone());
            teacherClassVo.setClassId(teacherClassRelation.getClassId());
            ClassInfo classInfo = classInfoRepository.selectByClassId(teacherClassVo.getClassId());
            if (classInfo != null) {
                teacherClassVo.setClassName(classInfo.getName());
                teacherClassVo.setProfessionId(classInfo.getProfessionId());
                ProfessionInfo professionInfo =
                        professionInfoRepository.selectByProfessionId(classInfo.getProfessionId());
                CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(classInfo.getCollegeId());
                teacherClassVo.setCollegeId(classInfo.getCollegeId());
                if (professionInfo != null) {
                    teacherClassVo.setProfessionName(professionInfo.getName());
                }
                if (collegeInfo != null) {
                    teacherClassVo.setCollegeName(collegeInfo.getName());
                }

            }
            if (StringUtils.containsIgnoreCase(teacherClassVo.getTeacherName(), search) || StringUtils
                    .containsIgnoreCase(teacherClassVo.getClassName(), search)) {
                teacherClassVoList.add(teacherClassVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), teacherClassVoList.size());
        return RestListData.create(teacherClassVoList.size(), teacherClassVoList.subList(start, end));
    }

    public Map<String, Object> deleteTeacherClass(Long recordId) {
        TeacherClassRelation teacherClassRelation = teacherClassRelationRepository.selectByRecordId(recordId);
        if (teacherClassRelation == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "记录不存在");
        }
        teacherClassRelation.setDeleted(DeleteStatusEnums.DELETED.getCode());
        teacherClassRelationRepository.updateById(teacherClassRelation);
        return Maps.newHashMap();
    }

    public Map<String, Object> divideClass(Long teacherId, List<Long> classIds) {
        classIds.forEach(classId -> {
            TeacherClassRelation teacherClassRelation = new TeacherClassRelation();
            teacherClassRelation.setTeacherId(teacherId);
            teacherClassRelation.setClassId(classId);
            teacherClassRelation.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            teacherClassRelation.setCreateTime(System.currentTimeMillis());
            teacherClassRelation.setUpdateTime(System.currentTimeMillis());
            if (teacherClassRelationRepository.selectByTeacherIdAndClassId(teacherId, classId) == null) {
                teacherClassRelationRepository.insert(teacherClassRelation);
            }
        });
        return Maps.newHashMap();
    }

    public RestListData<StudentVo> getStudentList(Long collegeId, Long teacherId,
            PageRequestParam pageRequestParam, String search) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<StudentVo> studentVoList = Lists.newArrayList();
        List<StudentInfo> studentInfoList;
        if (collegeId == null && teacherId == null) {
            studentInfoList = studentInfoRepository.getStudentList();
        } else if (collegeId == null) {
            List<TeacherClassRelation> teacherClassRelationList =
                    teacherClassRelationRepository.selectByTeacherId(teacherId);
            if (CollectionUtils.isEmpty(teacherClassRelationList)) {
                return RestListData.create(studentVoList.size(), studentVoList);
            }
            List<Long> classIds = Lists.newArrayList();
            teacherClassRelationList.forEach(teacherClassRelation -> {
                classIds.add(teacherClassRelation.getClassId());
            });
            if (CollectionUtils.isEmpty(classIds)) {
                return RestListData.create(studentVoList.size(), studentVoList);
            }
            studentInfoList = studentInfoRepository.selectStudentListByClassIds(classIds);
        } else {
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(collegeId);
            if (collegeInfo == null) {
                return RestListData.create(studentVoList.size(), studentVoList);
            }
            List<ClassInfo> classInfoList = classInfoRepository.selectByCollegeId(collegeId);
            if (CollectionUtils.isEmpty(classInfoList)) {
                return RestListData.create(studentVoList.size(), studentVoList);
            }
            List<Long> classIds = Lists.newArrayList();
            classInfoList.forEach(classInfo -> {
                classIds.add(classInfo.getId());
            });
            if (CollectionUtils.isEmpty(classIds)) {
                return RestListData.create(studentVoList.size(), studentVoList);
            }
            studentInfoList = studentInfoRepository.selectStudentListByClassIds(classIds);
        }
        if (CollectionUtils.isEmpty(studentInfoList)) {
            return RestListData.create(studentVoList.size(), studentVoList);
        }
        String finalSearch = search;
        studentInfoList.forEach(studentInfo -> {
            StudentVo studentVo = getStudentVo(studentInfo);
            if (StringUtils.containsIgnoreCase(studentVo.getStudentName(), finalSearch) || StringUtils
                    .containsIgnoreCase(studentVo.getClassName(), finalSearch) || StringUtils
                    .containsIgnoreCase(studentVo.getCollegeName(), finalSearch) || StringUtils
                    .containsIgnoreCase(studentVo.getProfessionName(), finalSearch)) {
                studentVoList.add(studentVo);
            }
        });
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), studentVoList.size());
        return RestListData.create(studentVoList.size(), studentVoList.subList(start, end));
    }

    public StudentVo getStudentVo(StudentInfo studentInfo) {
        StudentVo studentVo = new StudentVo();
        studentVo.setStudentId(studentInfo.getStudentId());
        studentVo.setStudentName(studentInfo.getName());
        if (studentInfo.getStudentNumber() != 0) {
            studentVo.setStudentNumber(studentInfo.getStudentNumber());
        }
        studentVo.setNativePlace(studentInfo.getNativePlace());
        studentVo.setNowPlace(studentInfo.getNowPlace());
        studentVo.setSex(studentInfo.getSex());
        ClassInfo classInfo = classInfoRepository.selectByClassId(studentInfo.getClassId());
        if (classInfo != null) {
            studentVo.setClassId(classInfo.getId());
            studentVo.setClassName(classInfo.getName());
            studentVo.setProfessionId(classInfo.getProfessionId());
            studentVo.setCollegeId(classInfo.getCollegeId());
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(classInfo.getCollegeId());
            if (collegeInfo != null) {
                studentVo.setCollegeName(collegeInfo.getName());
            }
            ProfessionInfo professionInfo =
                    professionInfoRepository.selectByProfessionId(classInfo.getProfessionId());
            if (professionInfo != null) {
                studentVo.setProfessionName(professionInfo.getName());
            }
        }
        List<StudentTaskArticle> studentTaskArticleList =
                studentTaskArticleRepository.selectByStudentId(studentInfo.getStudentId());
        if (CollectionUtils.isEmpty(studentTaskArticleList)) {
            studentVo.setStatus(0);
        } else {
            studentVo.setStatus(1);
            studentTaskArticleList.forEach(studentTaskArticle -> {
                if (studentTaskArticle.getStatus().equals(StudentTaskStatusEnums.UNKNOWN.getCode())
                        || studentTaskArticle.getStatus().equals(StudentTaskStatusEnums.FAIL.getCode())) {
                    studentVo.setStatus(0);
                }
            });
        }
        return studentVo;
    }

    public RestListData<ArticleVo> getArticleList(PageRequestParam pageRequestParam, String search) {
        search = (search == null) ? StringUtils.EMPTY : search;
        List<ArticleVo> articleVoList = Lists.newArrayList();
        List<ArticleInfo> articleInfoList = articleInfoRepository.getArticleList();
        if (CollectionUtils.isEmpty(articleInfoList)) {
            return RestListData.create(articleVoList.size(), articleVoList);
        }
        for (ArticleInfo articleInfo : articleInfoList) {
            ArticleVo articleVo = new ArticleVo();
            articleVo.setArticleId(articleInfo.getId());
            articleVo.setArticleName(articleInfo.getName());
            if (articleInfo.getType() != 0) {
                articleVo.setArticleType(articleInfo.getType());
            }
            articleVo.setStartTime(articleInfo.getStartTime());
            articleVo.setEndTime(articleInfo.getEndTime());
            if (StringUtils.containsIgnoreCase(articleVo.getArticleName(), search)) {
                articleVoList.add(articleVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), articleVoList.size());
        return RestListData.create(articleVoList.size(), articleVoList.subList(start, end));
    }

    public RestRsp<Map<String, Object>> uploadFile(MultipartFile file, Integer type) {
        Map<String, Object> objectMap = Maps.newHashMap();
        try {
            if (file.isEmpty()) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件不能为空");
            }
            String fileName = file.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "文件名不能为空");
            }
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            File path = new File(System.getProperty("user.dir") + "/src/main/resources");
            File upload = new File(path.getAbsolutePath(), "/file");
            File dest = new File(upload.getAbsolutePath() + "/" + fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setName(fileName);
            articleInfo.setPath("/src/main/resources/file/" + fileName);
            articleInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            articleInfo.setTemplate(TemplateEnums.YES.getCode());
            articleInfo.setStartTime(System.currentTimeMillis());
            articleInfo.setEndTime(System.currentTimeMillis());
            articleInfoRepository.insert(articleInfo);
            if (type != null) {
                //todo 存储数据库
            }
            return RestRsp.success(objectMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestRsp.fail(ErrorCode.SERVER_ERROR, "上传失败");
    }

    public Map<String, Object> editArticle(EditArticleReq editArticleReq) {
        ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(editArticleReq.getArticleId());
        if (articleInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该文件不存在");
        }
        articleInfo.setStartTime(editArticleReq.getStartTime());
        articleInfo.setEndTime(editArticleReq.getEndTime());
        articleInfo.setType(editArticleReq.getArticleType());
        articleInfoRepository.updateById(articleInfo);
        return Maps.newHashMap();
    }

    public Map<String, Object> deleteArticle(Long articleId) {
        ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(articleId);
        if (articleInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该文件不存在");
        }
        articleInfo.setDeleted(DeleteStatusEnums.DELETED.getCode());
        articleInfoRepository.updateById(articleInfo);
        File file = new File(System.getProperty("user.dir") + articleInfo.getPath());
        if (file.exists()) {
            file.delete();
        }
        return Maps.newHashMap();
    }

    public void previewArticle(Long articleId, HttpServletResponse response) {
        ArticleInfo articleInfo = articleInfoRepository.selectById(articleId);
        if (articleInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "文件不存在");
        }
        File file = new File(System.getProperty("user.dir") + articleInfo.getPath());//需要转换的文件
        try {
            File newFile = new File(System.getProperty("user.dir") + "/src/main/resources/pdf");//转换之后文件生成的地址
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            String savePath = newFile.getAbsolutePath() + "/"; //pdf文件生成保存的路径
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
            String fileType = ".pdf"; //pdf文件后缀
            String newFileMix = savePath + fileName + fileType;  //将这三个拼接起来,就是我们最后生成文件保存的完整访问路径了
            String suffixName = articleInfo.getPath().substring(articleInfo.getPath().lastIndexOf("."));
            converter.convert(file).to(new File(newFileMix)).execute();
            //使用response,将pdf文件以流的方式发送的前端浏览器上
            response.setContentType("application/pdf");
            //            response.setContentType(MediaType.ALL_VALUE);
            response.setHeader("Content-Disposition",
                    "inline;fileName=" + URLEncoder.encode(fileName + fileType, "UTF-8"));
            ServletOutputStream servletoutputStream = response.getOutputStream();
            InputStream in = new FileInputStream(new File(newFileMix));// 读取文件
            FileCopyUtils.copy(in, servletoutputStream);
            //int i = IOUtils.copy(in, servletoutputStream);   // copy流数据,i为字节数
            in.close();
            servletoutputStream.close();
            //log.info("流已关闭,可预览,该文件字节大小：" + i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RestListData<TeacherTaskReleaseVo> getTeacherReleaseList(Long teacherId, Long collegeId,
            PageRequestParam pageRequestParam, String search) {
        List<TeacherTaskReleaseVo> teacherTaskReleaseVos = Lists.newArrayList();
        List<TeacherReleaseRecord> teacherReleaseRecordList;
        if (teacherId == null && collegeId == null) {
            teacherReleaseRecordList = teacherReleaseRecordRepository.selectAllList();
        } else if (teacherId == null) {
            List<ClassInfo> classInfoList = classInfoRepository.selectByCollegeId(collegeId);
            if (CollectionUtils.isEmpty(classInfoList)) {
                return RestListData.create(teacherTaskReleaseVos.size(), teacherTaskReleaseVos);
            }
            List<Long> classIds = Lists.newArrayList();
            for (ClassInfo classInfo : classInfoList) {
                classIds.add(classInfo.getId());
            }
            teacherReleaseRecordList = teacherReleaseRecordRepository.selectByClassIds(classIds);
        } else {
            teacherReleaseRecordList = teacherReleaseRecordRepository.selectByTeacherId(teacherId);
        }
        if (CollectionUtils.isEmpty(teacherReleaseRecordList)) {
            return RestListData.create(teacherTaskReleaseVos.size(), teacherTaskReleaseVos);
        }
        for (TeacherReleaseRecord teacherReleaseRecord : teacherReleaseRecordList) {
            TeacherTaskReleaseVo teacherTaskReleaseVo = new TeacherTaskReleaseVo();
            teacherTaskReleaseVo.setRecordId(teacherReleaseRecord.getId());
            teacherTaskReleaseVo.setArticleId(teacherReleaseRecord.getArticleId());
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(teacherReleaseRecord.getArticleId());
            if (articleInfo != null) {
                teacherTaskReleaseVo.setArticleName(articleInfo.getName());
            }
            teacherTaskReleaseVo.setReleaseTime(teacherReleaseRecord.getReleaseTime());
            teacherTaskReleaseVo.setTeacherId(teacherReleaseRecord.getTeacherId());
            UserInfo userInfo = userInfoRepository.selectByUserId(teacherReleaseRecord.getTeacherId());
            if (userInfo != null) {
                teacherTaskReleaseVo.setTeacherName(userInfo.getName());
            }
            ClassInfo classInfo = classInfoRepository.selectByClassId(teacherReleaseRecord.getClassId());
            if (classInfo != null) {
                teacherTaskReleaseVo.setClassName(classInfo.getName());
                CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(classInfo.getCollegeId());
                ProfessionInfo professionInfo =
                        professionInfoRepository.selectByProfessionId(classInfo.getProfessionId());
                if (collegeInfo != null) {
                    teacherTaskReleaseVo.setCollegeName(collegeInfo.getName());
                }
                if (professionInfo != null) {
                    teacherTaskReleaseVo.setProfessionName(professionInfo.getName());
                }
            }
            if (StringUtils.containsIgnoreCase(teacherTaskReleaseVo.getArticleName(), search) || StringUtils
                    .containsIgnoreCase(teacherTaskReleaseVo.getTeacherName(), search)) {
                teacherTaskReleaseVos.add(teacherTaskReleaseVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), teacherTaskReleaseVos.size());
        return RestListData.create(teacherTaskReleaseVos.size(), teacherTaskReleaseVos.subList(start, end));
    }

    public RestListData<StudentGradeRecordVo> getStudentGradeList(Long teacherId, Long collegeId,
            PageRequestParam pageRequestParam, String search) {
        List<StudentGradeRecordVo> studentGradeRecordVoList = Lists.newArrayList();
        List<StudentGradeRecord> studentGradeRecordList;
        if (teacherId == null && collegeId == null) {
            studentGradeRecordList = studentGradeRecordRepository.selectAllList();
        } else if (teacherId == null) {
            List<ClassInfo> classInfoList = classInfoRepository.selectByCollegeId(collegeId);
            List<Long> classIds = Lists.newArrayList();
            if (CollectionUtils.isEmpty(classInfoList)) {
                return RestListData.create(studentGradeRecordVoList.size(), studentGradeRecordVoList);
            }
            for (ClassInfo classInfo : classInfoList) {
                classIds.add(classInfo.getId());
            }
            studentGradeRecordList = studentGradeRecordRepository.selectByClassIds(classIds);
        } else {
            studentGradeRecordList = studentGradeRecordRepository.selectByTeacherId(teacherId);
        }
        for (StudentGradeRecord studentGradeRecord : studentGradeRecordList) {
            StudentGradeRecordVo studentGradeRecordVo = new StudentGradeRecordVo();
            studentGradeRecordVo.setRecordId(studentGradeRecord.getId());
            studentGradeRecordVo.setArticleId(studentGradeRecord.getArticleId());
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(studentGradeRecord.getArticleId());
            if (articleInfo != null) {
                studentGradeRecordVo.setArticleName(articleInfo.getName());
            }
            StudentInfo studentInfo = studentInfoRepository.selectByStudentId(studentGradeRecord.getStudentId());
            if (studentInfo != null) {
                studentGradeRecordVo.setStudentName(studentInfo.getName());
            }
            studentGradeRecordVo.setReleaseTime(studentGradeRecord.getReleaseTime());
            UserInfo userInfo = userInfoRepository.selectByUserId(studentGradeRecord.getTeacherId());
            studentGradeRecordVo.setTeacherId(studentGradeRecord.getTeacherId());
            if (userInfo != null) {
                studentGradeRecordVo.setTeacherName(userInfo.getName());
            }
            ClassInfo classInfo = classInfoRepository.selectByClassId(studentGradeRecord.getClassId());
            if (classInfo != null) {
                CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(classInfo.getCollegeId());
                ProfessionInfo professionInfo =
                        professionInfoRepository.selectByProfessionId(classInfo.getProfessionId());
                if (professionInfo != null) {
                    studentGradeRecordVo.setProfessionName(professionInfo.getName());
                }
                if (collegeInfo != null) {
                    studentGradeRecordVo.setCollegeName(collegeInfo.getName());
                }
                studentGradeRecordVo.setClassName(classInfo.getName());
            }
            if (StringUtils.containsIgnoreCase(studentGradeRecordVo.getArticleName(), search) || StringUtils
                    .containsIgnoreCase(studentGradeRecordVo.getTeacherName(), search) || StringUtils
                    .containsIgnoreCase(studentGradeRecordVo.getStudentName(), search)) {
                studentGradeRecordVoList.add(studentGradeRecordVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), studentGradeRecordVoList.size());
        return RestListData.create(studentGradeRecordVoList.size(), studentGradeRecordVoList.subList(start, end));
    }

    public Map<String, Object> deleteRecord(Long recordId, Integer type) {
        if (type.equals(RecordTypeEnums.TEACHER.getCode())) {
            TeacherReleaseRecord teacherReleaseRecord = teacherReleaseRecordRepository.selectByRecordId(recordId);
            if (teacherReleaseRecord == null) {
                throw ServiceException.of(ErrorCode.NOT_FOUNT, "该记录不存在");
            }
            teacherReleaseRecord.setDeleted(DeleteStatusEnums.DELETED.getCode());
            teacherReleaseRecordRepository.updateById(teacherReleaseRecord);
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(teacherReleaseRecord.getArticleId());
            if (articleInfo != null) {
                deleteArticle(articleInfo.getId());
            }
        } else {
            StudentGradeRecord studentGradeRecord = studentGradeRecordRepository.selectByRecordId(recordId);
            if (studentGradeRecord == null) {
                throw ServiceException.of(ErrorCode.NOT_FOUNT, "该记录不存在");
            }
            ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(studentGradeRecord.getArticleId());
            studentGradeRecord.setDeleted(DeleteStatusEnums.DELETED.getCode());
            studentGradeRecordRepository.updateById(studentGradeRecord);
            if (articleInfo != null) {
                deleteArticle(articleInfo.getId());
            }
        }
        return Maps.newHashMap();
    }

    public Map<String, Object> articleDownload(Long articleId, HttpServletResponse response) {
        ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(articleId);
        if (articleInfo == null) {
            throw ServiceException.of(ErrorCode.NOT_FOUNT, "该文件不存在");
        }
        File file = new File(System.getProperty("user.dir") + articleInfo.getPath());
        if (file.exists()) {
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + articleInfo.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return Maps.newHashMap();
    }

    public RestListData<StudentTaskArticleVo> getStudentTaskList(Long teacherId, Long collegeId,
            PageRequestParam pageRequestParam, String search) {
        List<StudentTaskArticleVo> studentTaskArticleVoList = Lists.newArrayList();
        List<StudentTaskArticle> studentTaskArticleList;
        if (teacherId == null && collegeId == null) {
            studentTaskArticleList = studentTaskArticleRepository.selectByStatus();
        } else if (teacherId == null) {
            List<Long> classIds = Lists.newArrayList();
            List<ClassInfo> classInfoList = classInfoRepository.selectByCollegeId(collegeId);
            if (CollectionUtils.isEmpty(classInfoList)) {
                return RestListData.create(studentTaskArticleVoList.size(), studentTaskArticleVoList);
            }
            classInfoList.forEach(classInfo -> {
                classIds.add(classInfo.getId());
            });
            studentTaskArticleList = studentTaskArticleRepository.selectByClassIds(classIds);
        } else {
            studentTaskArticleList = studentTaskArticleRepository.selectByTeacherId(teacherId);
        }
        for (StudentTaskArticle studentTaskArticle : studentTaskArticleList) {
            StudentTaskArticleVo studentTaskArticleVo = getStudentTaskArticleVo(studentTaskArticle);
            if (StringUtils.containsIgnoreCase(studentTaskArticleVo.getArticleName(), search) || StringUtils
                    .containsIgnoreCase(studentTaskArticleVo.getStudentName(), search)) {
                studentTaskArticleVoList.add(studentTaskArticleVo);
            }
        }
        int start = pageRequestParam.getStart();
        int end = Math.min(start + pageRequestParam.getPageSize(), studentTaskArticleVoList.size());
        return RestListData.create(studentTaskArticleVoList.size(), studentTaskArticleVoList.subList(start, end));
    }

    public StudentTaskArticleVo getStudentTaskArticleVo(StudentTaskArticle studentTaskArticle) {
        StudentTaskArticleVo studentTaskArticleVo = new StudentTaskArticleVo();
        studentTaskArticleVo.setArticleId(studentTaskArticle.getArticleId());
        studentTaskArticleVo.setTaskId(studentTaskArticle.getId());
        StudentInfo studentInfo = studentInfoRepository.selectByStudentId(studentTaskArticle.getStudentId());
        if (studentInfo != null) {
            studentTaskArticleVo.setStudentName(studentInfo.getName());
        }
        ArticleInfo articleInfo = articleInfoRepository.selectByArticleId(studentTaskArticle.getArticleId());
        if (articleInfo != null) {
            studentTaskArticleVo.setArticleName(articleInfo.getName());
        }
        studentTaskArticleVo.setUpdateTime(studentTaskArticle.getUpdateTime());
        ClassInfo classInfo = classInfoRepository.selectByClassId(studentTaskArticle.getClassId());
        if (classInfo != null) {
            ProfessionInfo professionInfo =
                    professionInfoRepository.selectByProfessionId(classInfo.getProfessionId());
            CollegeInfo collegeInfo = collegeInfoRepository.selectByCollegeId(classInfo.getCollegeId());
            if (collegeInfo != null) {
                studentTaskArticleVo.setCollegeName(collegeInfo.getName());
            }
            if (professionInfo != null) {
                studentTaskArticleVo.setProfessionName(professionInfo.getName());
            }
            studentTaskArticleVo.setClassName(classInfo.getName());
        }
        return studentTaskArticleVo;
    }

    public RestListData<Map<String, Object>> getStudentAttendance() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<StudentAttendance> studentAttendances = studentAttendanceRepository.selectListAll();
        int overNinety = 0;
        int overEight = 0;
        int overSixty = 0;
        int blowSixty = 0;
        if (!CollectionUtils.isEmpty(studentAttendances)) {
            for (StudentAttendance studentAttendance : studentAttendances) {
                if (studentAttendance.getAttendance() >= 0.9) {
                    overNinety++;
                } else if (studentAttendance.getAttendance() >= 0.8 && studentAttendance.getAttendance() < 0.9) {
                    overEight++;
                } else if (studentAttendance.getAttendance() >= 0.6 && studentAttendance.getAttendance() < 0.8) {
                    overSixty++;
                } else {
                    blowSixty++;
                }
            }
        }
        Map<String, Object> overNinetyMap = Maps.newHashMap();
        overNinetyMap.put("name", "出勤率90%及以上");
        overNinetyMap.put("value", overNinety);
        mapList.add(overNinetyMap);
        Map<String, Object> overEightMap = Maps.newHashMap();
        overEightMap.put("name", "出勤率在80%-90%");
        overEightMap.put("value", overEight);
        mapList.add(overEightMap);
        Map<String, Object> overSixtyMap = Maps.newHashMap();
        overSixtyMap.put("name", "出勤率在60%-80%");
        overSixtyMap.put("value", overSixty);
        mapList.add(overSixtyMap);
        Map<String, Object> blowSixtyMap = Maps.newHashMap();
        blowSixtyMap.put("name", "出勤率在60%以下");
        blowSixtyMap.put("value", blowSixty);
        mapList.add(blowSixtyMap);
        return RestListData.create(mapList.size(), mapList);
    }

    public RestListData<Map<String, Object>> getStudentGrade() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<StudentGrade> studentGrades = studentGradeRepository.selectListAll();
        int overNinety = 0;
        int overEight = 0;
        int overSixty = 0;
        int blowSixty = 0;
        if (!CollectionUtils.isEmpty(studentGrades)) {
            for (StudentGrade studentGrade : studentGrades) {
                if (studentGrade.getGrade() >= 90) {
                    overNinety++;
                } else if (studentGrade.getGrade() >= 80 && studentGrade.getGrade() < 90) {
                    overEight++;
                } else if (studentGrade.getGrade() >= 60 && studentGrade.getGrade() < 80) {
                    overSixty++;
                } else {
                    blowSixty++;
                }
            }
        }
        Map<String, Object> overNinetyMap = Maps.newHashMap();
        overNinetyMap.put("name", "优秀(90分及以上)");
        overNinetyMap.put("value", overNinety);
        mapList.add(overNinetyMap);
        Map<String, Object> overEightMap = Maps.newHashMap();
        overEightMap.put("name", "良好(80分-90分)");
        overEightMap.put("value", overEight);
        mapList.add(overEightMap);
        Map<String, Object> overSixtyMap = Maps.newHashMap();
        overSixtyMap.put("name", "及格(60分-80分)");
        overSixtyMap.put("value", overSixty);
        mapList.add(overSixtyMap);
        Map<String, Object> blowSixtyMap = Maps.newHashMap();
        blowSixtyMap.put("name", "不及格(60分以下)");
        blowSixtyMap.put("value", blowSixty);
        mapList.add(blowSixtyMap);
        return RestListData.create(mapList.size(), mapList);
    }

    public RestListData<List<Double>> getStudentAttendanceRelation() {
        List<List<Double>> list = Lists.newArrayList();
        List<StudentAttendance> studentAttendances = studentAttendanceRepository.selectListAll();
        List<StudentGrade> studentGrades = studentGradeRepository.selectListAll();
        if (!CollectionUtils.isEmpty(studentAttendances) && !CollectionUtils.isEmpty(studentGrades)) {
            for (StudentAttendance studentAttendance : studentAttendances) {
                List<Double> integerList = Lists.newArrayList();
                for (StudentGrade studentGrade : studentGrades) {
                    if (studentGrade.getStudentName().equals(studentAttendance.getStudentName())) {
                        integerList.add(studentAttendance.getAttendance());
                        integerList.add(studentGrade.getGrade());
                        list.add(integerList);
                        break;
                    }
                }
            }
        }
        return RestListData.create(list.size(), list);
    }

    public void insert() {
        for (int i = 0; i < 100; i++) {
            StudentAttendance studentAttendance = new StudentAttendance();
            studentAttendance.setAttendance(0.5 + RandomUtils.nextDouble(0, 1));
            if (studentAttendance.getAttendance() > 1) {
                studentAttendance.setAttendance(1.0 - RandomUtils.nextDouble(0, 0.2) + RandomUtils.nextDouble(0, 0.2));
            }
            if (studentAttendance.getAttendance() > 1) {
                studentAttendance.setAttendance(1.0);
            }
            studentAttendance.setStudentName(String.valueOf(i));
            studentAttendance.setCreateTime(System.currentTimeMillis());
            studentAttendance.setUpdateTime(System.currentTimeMillis());
            studentAttendance.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            StudentGrade studentGrade = new StudentGrade();
            studentGrade.setGrade(
                    studentAttendance.getAttendance() * 100 - RandomUtils.nextInt(0, 20) + RandomUtils.nextInt(0, 20));
            studentGrade.setCreateTime(System.currentTimeMillis());
            if (studentGrade.getGrade() > 100) {
                studentGrade.setGrade(100.0);
            }
            studentGrade.setUpdateTime(System.currentTimeMillis());
            studentGrade.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            studentGrade.setStudentName(String.valueOf(i));
            studentGradeRepository.insert(studentGrade);
            studentAttendanceRepository.insert(studentAttendance);
        }

    }

}
