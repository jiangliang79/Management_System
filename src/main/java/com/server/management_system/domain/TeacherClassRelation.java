package com.server.management_system.domain;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class TeacherClassRelation {
    private Long id;
    private Long teacherId;
    private Long classId;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
