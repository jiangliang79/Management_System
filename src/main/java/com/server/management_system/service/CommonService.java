package com.server.management_system.service;


import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.StudentInfoRepository;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.StudentInfo;
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.enums.LoginStatusEnums;
import com.server.management_system.enums.UserTypeEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.util.TokenUtil;
import com.server.management_system.vo.LoginVo;
import com.server.management_system.vo.req.RegisterReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@Service
public class CommonService {
    @Resource
    private UserInfoRepository userInfoRepository;
    @Resource
    private StudentInfoRepository studentInfoRepository;

    public LoginVo login(String username, String password) {
        LoginVo loginVo = new LoginVo();
        UserInfo userInfo = userInfoRepository.selectByUserName(username);
        if (userInfo == null) {
            loginVo.setLoginStatus(LoginStatusEnums.FAIL.getCode());
        } else if (userInfo.getPassword().equals(password)) {
            if (StringUtils.isNotEmpty(userInfo.getToken())) {
                throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前用户已经登录");
            }
            loginVo.setUserId(userInfo.getId());
            loginVo.setType(userInfo.getType());
            loginVo.setUsername(username);
            loginVo.setLoginStatus(LoginStatusEnums.SUCCESS.getCode());
            String token = TokenUtil.getToken(username);
            loginVo.setAuthentication(username + " " + token);
            userInfo.setToken(username + " " + token);
            userInfoRepository.updateById(userInfo);
        } else {
            loginVo.setLoginStatus(LoginStatusEnums.FAIL.getCode());
        }
        return loginVo;
    }

    public Map<String, Object> register(RegisterReq registerReq) {
        UserInfo userInfo = userInfoRepository.selectByUserName(registerReq.getUsername());
        if (userInfo != null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "当前用户已经存在");
        }
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUsername(registerReq.getUsername());
        newUserInfo.setPassword(registerReq.getPassword());
        newUserInfo.setName(registerReq.getName());
        newUserInfo.setType(registerReq.getType());
        newUserInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
        newUserInfo.setPhone(registerReq.getPhone());
        newUserInfo.setCreateTime(System.currentTimeMillis());
        newUserInfo.setUpdateTime(System.currentTimeMillis());
        userInfoRepository.insert(newUserInfo);
        if (registerReq.getType().equals(UserTypeEnums.STUDENT.getCode())) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setStudentId(newUserInfo.getId());
            studentInfo.setSex(registerReq.getSex());
            studentInfo.setName(registerReq.getName());
            studentInfo.setPhone(registerReq.getPhone());
            studentInfo.setDeleted(DeleteStatusEnums.NOT_DELETE.getCode());
            studentInfo.setCreateTime(System.currentTimeMillis());
            studentInfo.setUpdateTime(System.currentTimeMillis());
            studentInfoRepository.insert(studentInfo);
        }
        return Maps.newHashMap();
    }

    public Map<String, Object> logout(String username) {
        UserInfo userInfo = userInfoRepository.selectByUserName(username);
        if (userInfo == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID,"用户不存在");
        }
        userInfo.setToken(StringUtils.EMPTY);
        userInfoRepository.updateById(userInfo);
        return Maps.newHashMap();
    }
}
