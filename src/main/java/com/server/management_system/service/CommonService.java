package com.server.management_system.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.server.management_system.dao.UserInfoRepository;
import com.server.management_system.domain.UserInfo;
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
        return loginVo;
    }
}
