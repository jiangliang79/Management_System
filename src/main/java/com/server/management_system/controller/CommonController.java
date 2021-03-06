package com.server.management_system.controller;


import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.management_system.constant.ErrorCode;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.service.AdminService;
import com.server.management_system.service.CommonService;
import com.server.management_system.vo.LoginVo;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.req.LoginReq;
import com.server.management_system.vo.req.RegisterReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@RestController
@RequestMapping("api/system/management/common")
@CrossOrigin
public class CommonController {

    @Autowired
    private CommonService commonService;
    @Autowired
    private AdminService adminService;

    @PostMapping("login")
    public RestRsp<LoginVo> login(@RequestBody LoginReq loginReq) {
        if (StringUtils.isEmpty(loginReq.getUsername()) || StringUtils.isEmpty(loginReq.getPassword())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户名或密码不能为空");
        }
        return RestRsp.success(commonService.login(loginReq.getUsername(), loginReq.getPassword()));
    }

    @PostMapping("register")
    public RestRsp<Map<String, Object>> register(@RequestBody RegisterReq registerReq) {
        if (StringUtils.isEmpty(registerReq.getName()) || StringUtils.isEmpty(registerReq.getPassword()) || StringUtils
                .isEmpty(registerReq.getUsername()) || registerReq.getType() == null || registerReq.getPhone() == null
                || registerReq.getSex() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(commonService.register(registerReq));
    }

    @PostMapping("logout")
    public RestRsp<Map<String, Object>> logout(@RequestBody LoginReq loginReq) {
        if (StringUtils.isEmpty(loginReq.getUsername())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "用户名不能为空");
        }
        return RestRsp.success(commonService.logout(loginReq.getUsername()));
    }

    @GetMapping("insert/data")
    public void insertData() {
        adminService.insert();
    }


}
