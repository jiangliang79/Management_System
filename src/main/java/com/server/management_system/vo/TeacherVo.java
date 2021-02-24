package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class TeacherVo {
    private Long teacherId;
    private String teacherName;
    private Long phone;
    private String teacherAccountNumber;
}
