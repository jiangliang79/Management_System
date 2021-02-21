package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
public enum OperatorTypeEnums {
    ADD(0, "添加"),
    EDIT(1, "编辑");
    private int code;
    private String desc;

    OperatorTypeEnums(int code, String desc) {
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
