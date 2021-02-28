package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-26
 */
@Data
public class StudentTaskArticleVo {
    private Long taskId;
    private Long studentId;
    private String studentName;
    private Long articleId;
    private String articleName;
    private Long updateTime;
    private String collegeName;
    private String professionName;
    private String className;
    private Long createTime;
    private Long endTime;
}
