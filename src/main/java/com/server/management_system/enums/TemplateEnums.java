package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
public enum TemplateEnums {
    YES(1, "是"),
    NO(0, "否");
    private int code;
    private String desc;

    TemplateEnums(int code, String desc) {
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
