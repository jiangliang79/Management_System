package com.server.management_system.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.server.management_system.constant.ErrorCode;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.param.PageRequestParam;
import com.server.management_system.service.TeacherService;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.TeacherTaskArticleVo;
import com.server.management_system.vo.req.CheckTaskArticleReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@RestController
@RequestMapping("api/system/management")
@CrossOrigin
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("teacher/task/article/list")
    public RestRsp<RestListData<TeacherTaskArticleVo>> getTeacherTaskArticleList(PageRequestParam pageRequestParam,
            String search) {
        return RestRsp.success(teacherService.getTeacherTaskArticleList(pageRequestParam, search));
    }

    @PostMapping("teacher/task/article/release")
    public RestRsp<Map<String, Object>> teacherTaskRelease(Long teacherId, @RequestParam("file") MultipartFile file) {
        if (teacherId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "teacherId不能为空");
        }
        return teacherService.teacherTaskRelease(teacherId, file);
    }

    @PostMapping("teacher/task/article/check")
    public RestRsp<Map<String, Object>> checkTaskArticle(@RequestBody CheckTaskArticleReq checkTaskArticleReq) {
        if (checkTaskArticleReq.getTaskId() == null || checkTaskArticleReq.getStatus() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "taskId或status不能为空");
        }
        return RestRsp.success(teacherService
                .checkTaskArticle(checkTaskArticleReq.getTaskId(), checkTaskArticleReq.getStatus(),
                        checkTaskArticleReq.getRemark()));
    }


}
