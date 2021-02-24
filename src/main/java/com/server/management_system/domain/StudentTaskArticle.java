package com.server.management_system.domain;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Data
public class StudentTaskArticle {
    private Long id;
    private Long articleId;
    private Long studentId;
    private Long teacherId;
    private Long classId;
    private Integer status; //0：通过，1：未通过，2：未填写
    private String remark;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
