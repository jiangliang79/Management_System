package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Data
public class ArticleVo {
    private Long articleId;
    private String articleName;
    private Integer articleType;
    private Long startTime;
    private Long endTime;
}
