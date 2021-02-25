package com.server.management_system.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.ArticleInfoRepository;
import com.server.management_system.dao.ClassInfoRepository;
import com.server.management_system.dao.CollegeInfoRepository;
import com.server.management_system.dao.ProfessionInfoRepository;
import com.server.management_system.dao.StudentInfoRepository;
import com.server.management_system.dao.StudentTaskArticleRepository;
import com.server.management_system.dao.TeacherClassRelationRepository;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.ArticleInfo;
import com.server.management_system.domain.ClassInfo;
import com.server.management_system.domain.CollegeInfo;
import com.server.management_system.domain.ProfessionInfo;
import com.server.management_system.domain.StudentInfo;
import com.server.management_system.domain.StudentTaskArticle;
import com.server.management_system.domain.TeacherClassRelation;
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.DeleteOrganizationEnums;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.StudentTaskStatusEnums;
import com.server.management_system.enums.UserTypeEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.vo.ArticleVo;
import com.server.management_system.vo.ClassVo;
import com.server.management_system.vo.CollegeVo;
import com.server.management_system.vo.ProfessionVo;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.StudentVo;
import com.server.management_system.vo.TeacherClassVo;
import com.server.management_system.vo.TeacherVo;
import com.server.management_system.vo.UserVo;
import com.server.management_system.vo.req.AddClassReq;
import com.server.management_system.vo.req.AddProfessionReq;
import com.server.management_system.vo.req.AddUserReq;
import com.server.management_system.vo.req.DeleteOrganizationReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Service
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
            collegeInfo.setId(userInfo.getId());
            collegeInfo.setName(userInfo.getName());
            collegeInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            collegeInfo.setCreateTime(System.currentTimeMillis());
            collegeInfo.setUpdateTime(System.currentTimeMillis());
            collegeInfo.setDescription(StringUtils.EMPTY);
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
        }else {
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
            collegeVo.setCollegeId(collegeInfo.getId());
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
            professionVo.setDescription(StringUtils.EMPTY);
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
        professionInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        professionInfo.setDescription(StringUtils.EMPTY);
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
            StudentVo studentVo = new StudentVo();
            studentVo.setStudentId(studentInfo.getStudentId());
            studentVo.setStudentName(studentInfo.getName());
            studentVo.setStudentNumber(studentInfo.getStudentNumber());
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
                studentVo.setStatus(1);
            } else {
                studentVo.setStatus(1);
                studentTaskArticleList.forEach(studentTaskArticle -> {
                    if (studentTaskArticle.getStatus().equals(StudentTaskStatusEnums.UNKNOWN.getCode())
                            || studentTaskArticle.getStatus().equals(StudentTaskStatusEnums.FAIL.getCode())) {
                        studentVo.setStatus(0);
                    }
                });
            }
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
            articleVo.setArticleType(articleInfo.getType());
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

}
