package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-26
 */
public enum RecordTypeEnums {
    TEACHER(1, "老师发布任务记录"),
    STUDENT(2, "学生成绩评定记录");
    private int code;
    private String desc;

    RecordTypeEnums(int code, String desc) {
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
