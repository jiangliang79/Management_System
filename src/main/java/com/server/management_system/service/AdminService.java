package com.server.management_system.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.DeleteStatusEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.vo.req.AddUserReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Service
public class AdminService {
    @Resource
    private UserInfoRepository userInfoRepository;

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
}
