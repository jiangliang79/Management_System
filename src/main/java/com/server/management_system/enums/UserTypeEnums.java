package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
public enum UserTypeEnums {
    STUDENT(1, "学生"),
    TEACHER(2, "老师"),
    COLLEGE(3, "学院"),
    ADMIN(0, "管理员");
    private int code;
    private String desc;

    UserTypeEnums(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
