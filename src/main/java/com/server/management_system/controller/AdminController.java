package com.server.management_system.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.management_system.constant.ErrorCode;
import com.server.management_system.enums.OperatorTypeEnums;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.service.AdminService;
import com.server.management_system.vo.CollegeVo;
import com.server.management_system.vo.ProfessionVo;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.UserVo;
import com.server.management_system.vo.req.AddUserReq;
import com.server.management_system.vo.req.DeleteUserReq;

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

    @GetMapping("user/list")
    public RestRsp<RestListData<UserVo>> getUserList(PageRequestParam pageRequestParam, String search) {
        return RestRsp.success(adminService.getUserList(pageRequestParam, search));
    }

    @PostMapping("user/delete")
    public RestRsp<Map<String, Object>> deleteUser(@RequestBody DeleteUserReq deleteUserReq) {
        if (deleteUserReq.getType() == null || deleteUserReq.getType() <= 0 || deleteUserReq.getUserId() == null
                || deleteUserReq.getUserId() <= 0) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "类型或用户ID不为空");
        }
        return RestRsp.success(adminService.deleteUser(deleteUserReq.getUserId(), deleteUserReq.getType()));
    }

    @GetMapping("college/list")
    public RestRsp<RestListData<CollegeVo>> getCollegeList(PageRequestParam pageRequestParam, String search,
            Boolean isAll) {
        if (isAll == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(adminService.getCollegeList(pageRequestParam, search, isAll));
    }

    @GetMapping("profession/list")
    public RestRsp<RestListData<ProfessionVo>> getProfessionList(PageRequestParam pageRequestParam, String search,
            Long collegeId) {
        return RestRsp.success(adminService.getProfessionList(pageRequestParam, search, collegeId));
    }

}
