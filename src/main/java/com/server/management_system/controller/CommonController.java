package com.server.management_system.controller;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import com.server.management_system.constant.ErrorCode;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.service.CommonService;
import com.server.management_system.vo.LoginVo;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.req.LoginReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@RestController
@RequestMapping("api/system/management")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @PostMapping("login")
    public RestRsp<LoginVo> login(@RequestBody LoginReq loginReq) {
        if (StringUtils.isEmpty(loginReq.getUsername()) || StringUtils.isEmpty(loginReq.getPassword())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户名或密码不能为空");
        }
        return RestRsp.success(commonService.login(loginReq.getUsername(), loginReq.getPassword()));
    }
}
