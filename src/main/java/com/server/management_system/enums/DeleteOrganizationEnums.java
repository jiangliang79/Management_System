package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
public enum DeleteOrganizationEnums {
    COLLEGE(1, "学院"),
    PROFESSION(2, "专业"),
    CLASS(3, "班级");
    private int code;
    private String desc;

    DeleteOrganizationEnums(int code, String desc) {
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
