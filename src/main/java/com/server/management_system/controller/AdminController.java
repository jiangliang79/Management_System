package com.server.management_system.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.management_system.constant.ErrorCode;
import com.server.management_system.enums.OperatorTypeEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.service.AdminService;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.req.AddUserReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@RestController
@RequestMapping("api/system/management")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("user/add")
    public RestRsp<Map<String, Object>> addOrEditUser(@RequestBody AddUserReq addUserReq) {
        if (StringUtils.isEmpty(addUserReq.getUsername()) || StringUtils.isEmpty(addUserReq.getPassword())
                || StringUtils.isEmpty(addUserReq.getDescription()) || StringUtils
                .isEmpty(addUserReq.getName()) || addUserReq.getOperatorType() == null
                || addUserReq.getUserType() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "传参错误");
        }
        if (addUserReq.getOperatorType().equals(OperatorTypeEnums.ADD.getCode())) {
            return RestRsp.success(adminService.addUser(addUserReq));
        } else {
            return RestRsp.success(adminService.editUser(addUserReq));
        }
    }
}
