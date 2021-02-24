package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class AddClassReq {
    private Integer operatorType;
    private Long classId;
    private Long collegeId;
    private Long professionId;
    private String className;
}
