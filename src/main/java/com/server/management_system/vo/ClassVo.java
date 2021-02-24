package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class ClassVo {
    private Long classId;
    private String className;
    private Long professionId;
    private String professionName;
    private Long collegeId;
    private String collegeName;
    private String description;
    private Long createTime;
    private Long updateTime;
}
