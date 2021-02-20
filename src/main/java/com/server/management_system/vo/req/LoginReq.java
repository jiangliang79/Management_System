package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@Data
public class LoginReq {
    private String username;
    private String password;
}
