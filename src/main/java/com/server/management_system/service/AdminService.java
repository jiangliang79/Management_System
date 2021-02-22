package com.server.management_system.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.CollegeInfoRepository;
import com.server.management_system.dao.ProfessionInfoRepository;
import com.server.management_system.dao.StudentInfoRepository;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.CollegeInfo;
import com.server.management_system.domain.ProfessionInfo;
import com.server.management_system.domain.StudentInfo;
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.UserTypeEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.vo.CollegeVo;
import com.server.management_system.vo.ProfessionVo;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.UserVo;
import com.server.management_system.vo.req.AddUserReq;

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
        userInfo.setPassword(addUserReq.getPassword());
        userInfo.setType(addUserReq.getUserType());
        userInfo.setName(addUserReq.getName());
        userInfo.setDescription(addUserReq.getDescription());
        userInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setUpdateTime(System.currentTimeMillis());
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
}
