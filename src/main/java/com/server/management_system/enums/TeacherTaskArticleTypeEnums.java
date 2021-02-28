package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-28
 */
public enum TeacherTaskArticleTypeEnums {
    TASK(1, "任务列表"),
    GRADE(2, "成绩评定列表");
    private int code;
    private String desc;

    TeacherTaskArticleTypeEnums(int code, String desc) {
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
