package com.server.management_system.constant;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-13
 */
public interface ErrorCode {
    int SUCCESS = 200;
    int SERVER_ERROR = 500;
    int PARAM_INVALID = 400;
    int UNAUTHORIZED = 401;
    int NOT_FOUNT = 404;
    int PERMISSION_DENIED = 403;
}
