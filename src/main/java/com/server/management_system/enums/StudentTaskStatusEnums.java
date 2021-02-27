package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
public enum StudentTaskStatusEnums {
    SUCCESS(0, "通过"),
    FAIL(1, "未通过"),
    UNKNOWN(2, "未填写"),
    NOT_MARK(3, "待批阅");
    private int code;
    private String desc;

    StudentTaskStatusEnums(int code, String desc) {
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
