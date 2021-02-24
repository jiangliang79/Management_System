package com.server.management_system.vo.req;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Data
public class DeleteOrganizationReq {
    private Integer type; //1:学院，2：专业，3：班级
    private Long id;
}
