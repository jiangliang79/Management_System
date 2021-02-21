package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Data
public class AddUserReq {
    private Integer operatorType;
    private Integer userType;
    private String username;
    private String name;
    private String password;
    private String description;
}
