package com.server.management_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Data
public class CollegeInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long collegeId;
    private String name;
    private String description;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
