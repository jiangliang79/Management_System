package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
@Data
public class EditArticleReq {
    private Long articleId;
    private Integer articleType;
    private Long startTime;
    private Long endTime;
}
