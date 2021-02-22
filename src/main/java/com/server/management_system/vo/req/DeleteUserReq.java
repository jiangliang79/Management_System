package com.server.management_system.vo.req;


import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Data
public class DeleteUserReq {
    private Integer type;
    private Long userId;
}
