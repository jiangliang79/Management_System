package com.server.management_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
@Data
public class TeacherReleaseRecord {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long articleId;
    private Long teacherId;
    private Long classId;
    private Long releaseTime;
    private Integer deleted;
}
