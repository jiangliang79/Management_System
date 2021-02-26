package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-26
 */
@Data
public class StudentGradeRecordVo {
    private Long recordId;
    private Long articleId;
    private String articleName;
    private String studentName;
    private Long releaseTime;
    private Long teacherId;
    private String teacherName;
    private String collegeName;
    private String professionName;
    private String className;
}
