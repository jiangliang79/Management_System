package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-28
 */
@Data
public class StudentReq {
    /*
     "studentId": 1, //学生ID
    "studentName": "姜良", //学生姓名
    "classId": 12, //班级ID
    "studentNumber": 201712323289, //学号
    "nativePlace": "山东烟台", //籍贯
    "nowPlace": "北京昌平", //现居住地
    "sex": 0 //0:男，1:女
     */
    private Long studentId;
    private String studentName;
    private Long classId;
    private Long studentNumber;
    private String nativePlace;
    private String nowPlace;
    private Integer sex;
}
