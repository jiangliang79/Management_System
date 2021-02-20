package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
public enum DeleteStatusEnums {
    NOT_DELETE(0, "未删除"),
    DELETED(1, "已删除");
    private int code;
    private String desc;

    DeleteStatusEnums(int code, String desc) {
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
