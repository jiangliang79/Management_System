package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@Data
public class LoginVo {
    private Integer loginStatus;
    private Long userId;
    private String username;
    private Integer type;
    private String authentication;
}
