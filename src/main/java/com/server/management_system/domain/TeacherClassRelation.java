package com.server.management_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class TeacherClassRelation {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long teacherId;
    private Long classId;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
