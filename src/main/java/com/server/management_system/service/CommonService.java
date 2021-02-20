package com.server.management_system.service;


import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.server.management_system.constant.ErrorCode;
import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.UserInfo;
import com.server.management_system.enums.LoginStatusEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.util.TokenUtil;
import com.server.management_system.vo.LoginVo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@Service
public class CommonService {
    @Resource
    private UserInfoRepository userInfoRepository;

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
}
