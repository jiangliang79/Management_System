package com.server.management_system.param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Getter
@Setter
public class PageRequestParam {
    private Integer pageNo;
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo != null && pageNo > 0 ? pageNo : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 20;
    }

    public Integer getStart() { // 兼容mysql limit  #{start},#{limit}
        return (getPageNo() - 1) * getPageSize();
    }

    public Integer getLimit() { // 兼容mysql limit  #{start},#{limit}
        return getPageSize();
    }
}
