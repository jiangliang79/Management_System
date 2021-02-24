package com.server.management_system.domain;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class ClassInfo {
    private Long id;
    private String name;
    private Long collegeId;
    private Long professionId;
    private String description;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
