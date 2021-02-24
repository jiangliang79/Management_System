package com.server.management_system.vo.req;

import java.util.List;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Data
public class DivideClassReq {
    private Long teacherId;
    private List<Long> classId;
}
