package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Data
public class UserVo {
    private Long userId;
    private Integer userType;
    private String username;
    private String name;
    private String description;
    private Long createTime;
}
