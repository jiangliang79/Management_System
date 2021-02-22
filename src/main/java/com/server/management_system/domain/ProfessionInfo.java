package com.server.management_system.domain;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Data
public class ProfessionInfo {
    private Long id;
    private String name;
    private Long collegeId;
    private String description;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
