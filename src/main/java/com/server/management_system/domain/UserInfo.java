package com.server.management_system.domain;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-20
 */
@Data
public class UserInfo {
    private Long id;
    private String username;
    private String password;
    private String token;
    private String name;
    private Long phone;
    private Integer type;
    private String description;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
