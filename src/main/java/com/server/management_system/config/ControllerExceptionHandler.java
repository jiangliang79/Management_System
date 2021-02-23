package com.server.management_system.config;


import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


import com.server.management_system.constant.ErrorCode;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.vo.RestRsp;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-13
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public RestRsp<Object> handleException(ServiceException exception) {
        return ofMessage(exception.getCode(), exception.getMessage(), exception.getData());
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public RestRsp<Object> handleParameterException(BindException exception) {
        String errorMsg = "参数无效," + exception.getMessage();
        return ofMessage(ErrorCode.PARAM_INVALID, errorMsg);
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public RestRsp<Object> handleAllException() {
        String errorMsg = "系统异常";
        return ofMessage(ErrorCode.SERVER_ERROR, errorMsg);
    }

    public RestRsp<Object> ofMessage(int status, String msg) {
        return ofMessage(status, msg, null);
    }

    public RestRsp<Object> ofMessage(int status, String msg, Object data) {
        return new RestRsp<>(status, msg, data);
    }
}