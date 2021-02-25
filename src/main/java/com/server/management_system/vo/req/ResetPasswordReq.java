package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
@Data
public class ResetPasswordReq {
    private Long userId;
    private String password;
}
