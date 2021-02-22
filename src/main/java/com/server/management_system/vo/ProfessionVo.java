package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Data
public class ProfessionVo {
    private Long professionId;
    private String professionName;
    private Long collegeId;
    private String collegeName;
    private String description;
    private Long createTime;
    private Long updateTime;
}
