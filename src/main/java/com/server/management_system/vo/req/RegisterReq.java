package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Data
public class RegisterReq {
    private String username;
    private String password;
    private Integer type;
    private String name;
    private Long phone;
    private Integer sex;
}
