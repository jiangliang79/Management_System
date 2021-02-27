package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-27
 */
@Data
public class TeacherTaskArticleVo {
    private Long articleId;
    private String articleName;
    private Long startTime;
    private Long endTime;
}
