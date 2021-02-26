package com.server.management_system.domain;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-26
 */
@Data
public class StudentGradeRecord {
    private Long id;
    private Long articleId;
    private Long studentId;
    private Long teacherId;
    private Long classId;
    private Long releaseTime;
    private Integer deleted;
}
