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
import com.server.management_system.service.StudentService;
import com.server.management_system.vo.RestListData;
import com.server.management_system.vo.RestRsp;
import com.server.management_system.vo.StudentTaskArticleVo;
import com.server.management_system.vo.StudentTaskWriteRecordVo;
import com.server.management_system.vo.StudentVo;
import com.server.management_system.vo.TeacherTaskReleaseVo;
import com.server.management_system.vo.req.StudentReq;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@RestController
@RequestMapping("api/system/management")
@CrossOrigin
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping("student/info")
    public RestRsp<StudentVo> getStudentInfo(Long studentId) {
        if (studentId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "studentId不能为空");
        }
        return RestRsp.success(studentService.getStudentInfo(studentId));
    }

    @PostMapping("student/info")
    public RestRsp<Map<String, Object>> updateStudentInfo(@RequestBody StudentReq studentReq) {
        if (studentReq.getStudentId() == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "学生ID不能为空");
        }
        return RestRsp.success(studentService.updateStudentInfo(studentReq));
    }

    @GetMapping("student/teacher/task/record")
    public RestRsp<RestListData<TeacherTaskReleaseVo>> getTeacherTaskRecord(Long studentId) {
        if (studentId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "学生ID不能为空");
        }
        return RestRsp.success(studentService.getTeacherTaskRecord(studentId));
    }

    @GetMapping("student/article/task/list")
    public RestRsp<RestListData<StudentTaskArticleVo>> getTaskList(Long studentId) {
        if (studentId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "学生ID不能为空");
        }
        return RestRsp.success(studentService.getTaskList(studentId));
    }

    @GetMapping("student/teacher/task/write/record")
    public RestRsp<RestListData<StudentTaskWriteRecordVo>> getTaskWriteRecordList(Long studentId) {
        if (studentId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "学生ID不能为空");
        }
        return RestRsp.success(studentService.getTaskWriteRecordList(studentId));
    }

    @PostMapping("student/teacher/task/upload")
    public RestRsp<Map<String, Object>> uploadTeacherTask(Long taskId, Long studentId,
            @RequestParam("file") MultipartFile file) {
        if (studentId == null || taskId == null) {
            throw ServiceException.of(ErrorCode.PARAM_INVALID, "taskId或studentId不能为空");
        }
        return RestRsp.success(studentService.uploadTeacherTask(taskId, studentId, file));
    }

}
