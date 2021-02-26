package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-26
 */
@Data
public class DeleteRecordReq {
    private Long recordId;
    private Integer type; //1:老师发布任务记录，2：学生成绩评定记录
}
