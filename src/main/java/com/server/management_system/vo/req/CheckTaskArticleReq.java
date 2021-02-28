package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-28
 */
@Data
public class CheckTaskArticleReq {
    private Long taskId;
    private Integer status;
    private String remark;
}
