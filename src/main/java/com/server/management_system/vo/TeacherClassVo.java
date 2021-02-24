package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class TeacherClassVo {
    private Long recordId;
    private Long teacherId;
    private String teacherName;
    private Long phone;
    private String teacherAccountNumber;
    private String collegeName;
    private Long collegeId;
    private String professionName;
    private Long professionId;
    private String className;
    private Long classId;
}
