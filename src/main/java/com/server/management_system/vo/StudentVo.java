package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Data
public class StudentVo {
    private Long studentId;
    private String studentName;
    private Long classId;
    private String className;
    private Long professionId;
    private String professionName;
    private Long collegeId;
    private String collegeName;
    private Long studentNumber;
    private String nativePlace;
    private String nowPlace;
    private Integer sex;
    private Integer status;
}
