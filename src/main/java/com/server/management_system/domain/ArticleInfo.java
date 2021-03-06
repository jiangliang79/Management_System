package com.server.management_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Data
public class ArticleInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String path;
    private Integer type;
    private Integer template;
    private Long startTime;
    private Long endTime;
    private Integer deleted;
}
