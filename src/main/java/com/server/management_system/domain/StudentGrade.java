package com.server.management_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-03-02
 */
@Data
public class StudentGrade {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String studentName;
    private Double grade;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
