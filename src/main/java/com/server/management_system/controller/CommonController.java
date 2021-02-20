package com.server.management_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.management_system.service.CommonService;
import com.server.management_system.vo.LoginVo;
import com.server.management_system.vo.RestRsp;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@RestController
@RequestMapping("api/system/management")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @GetMapping("login")
    public RestRsp<LoginVo> login() {
        return RestRsp.success(commonService.login("",""));
    }
}
