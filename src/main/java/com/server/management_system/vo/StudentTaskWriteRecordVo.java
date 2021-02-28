package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-28
 */
@Data
public class StudentTaskWriteRecordVo {
    private Long recordId;
    private Long articleId;
    private String articleName;
    private Long releaseTime;
    private Integer status;
}
