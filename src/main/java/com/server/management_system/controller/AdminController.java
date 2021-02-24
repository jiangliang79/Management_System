package com.server.management_system.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.server.management_system.vo.ArticleVo;
import com.server.management_system.vo.ClassVo;
import com.server.management_system.vo.CollegeVo;
import com.server.management_system.vo.ProfessionVo;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.StudentVo;
import com.server.management_system.vo.TeacherClassVo;
import com.server.management_system.vo.TeacherVo;
import com.server.management_system.vo.UserVo;
import com.server.management_system.vo.req.AddClassReq;
import com.server.management_system.vo.req.AddProfessionReq;
import com.server.management_system.vo.req.AddUserReq;
import com.server.management_system.vo.req.DeleteOrganizationReq;
import com.server.management_system.vo.req.DeleteTeacherClassReq;
import com.server.management_system.vo.req.DeleteUserReq;
import com.server.management_system.vo.req.DivideClassReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@RestController
@RequestMapping("api/system/management")
@CrossOrigin
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

    @GetMapping("class/list")
    public RestRsp<RestListData<ClassVo>> getClassList(PageRequestParam pageRequestParam, String search,
            Long professionId) {
        return RestRsp.success(adminService.getClassList(pageRequestParam, search, professionId));
    }

    @PostMapping("class/add")
    public RestRsp<Map<String, Object>> addOrEditClass(@RequestBody AddClassReq addClassReq) {
        if (addClassReq.getOperatorType() == null || addClassReq.getCollegeId() == null
                || addClassReq.getProfessionId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        if (addClassReq.getOperatorType().equals(OperatorTypeEnums.ADD.getCode())) {
            return RestRsp.success(adminService.addClass(addClassReq));
        } else {
            return RestRsp.success(adminService.editClass(addClassReq));
        }
    }

    @PostMapping("profession/add")
    public RestRsp<Map<String, Object>> addOrEditProfession(@RequestBody AddProfessionReq addProfessionReq) {
        if (addProfessionReq.getOperatorType() == null || addProfessionReq.getCollegeId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        if (addProfessionReq.getOperatorType().equals(OperatorTypeEnums.ADD.getCode())) {
            return RestRsp.success(adminService.addProfession(addProfessionReq));
        } else {
            return RestRsp.success(adminService.editProfession(addProfessionReq));
        }
    }

    @PostMapping("organization/delete")
    public RestRsp<Map<String, Object>> deleteOrganization(@RequestBody DeleteOrganizationReq deleteOrganizationReq) {
        if (deleteOrganizationReq.getType() == null || deleteOrganizationReq.getId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(adminService.deleteOrganization(deleteOrganizationReq));
    }

    @GetMapping("teacher/list")
    public RestRsp<RestListData<TeacherVo>> getTeacherList(PageRequestParam pageRequestParam, String search) {
        return RestRsp.success(adminService.getTeacherList(pageRequestParam, search));
    }

    @GetMapping("teacher/class/list")
    public RestRsp<RestListData<TeacherClassVo>> getTeacherClassList(PageRequestParam pageRequestParam, String search) {
        return RestRsp.success(adminService.getTeacherClassList(pageRequestParam, search));
    }

    @PostMapping("teacher/class/delete")
    public RestRsp<Map<String, Object>> deleteTeacherClass(@RequestBody DeleteTeacherClassReq deleteTeacherClassReq) {
        if (deleteTeacherClassReq.getRecordId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(adminService.deleteTeacherClass(deleteTeacherClassReq.getRecordId()));
    }

    @PostMapping("class/divide")
    public RestRsp<Map<String, Object>> divideClass(@RequestBody DivideClassReq divideClassReq) {
        if (divideClassReq.getTeacherId() == null || CollectionUtils.isEmpty(divideClassReq.getClassId())) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "参数错误");
        }
        return RestRsp.success(adminService.divideClass(divideClassReq.getTeacherId(), divideClassReq.getClassId()));
    }

    @GetMapping("student/list")
    public RestRsp<RestListData<StudentVo>> getStudentList(Long collegeId, Long teacherId,
            PageRequestParam pageRequestParam, String search) {
        return RestRsp.success(adminService.getStudentList(collegeId, teacherId, pageRequestParam, search));
    }

    @GetMapping("article/list")
    public RestRsp<RestListData<ArticleVo>> getArticleList(PageRequestParam pageRequestParam, String search) {
        return RestRsp.success(adminService.getArticleList(pageRequestParam, search));
    }
}
