package com.server.management_system.enums;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
public enum ArticleTypeEnums {
    //1:老师评分表，2:学生填写表，3:实习任务表
    TEACHER(1, "老师评分表"),
    STUDENT(2, "学生填写表"),
    TASK(3, "实习任务表");
    private int code;
    private String desc;

    ArticleTypeEnums(int code, String desc) {
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
