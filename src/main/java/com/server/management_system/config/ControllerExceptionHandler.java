package com.server.management_system.config;


import javax.servlet.http.HttpServletRequest;

import org.springframework.core.log.LogMessage;
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
        String msg = exception.getCode() + exception.getMessage() + exception.getData();
        log.error(msg);
        return ofMessage(exception.getCode(), exception.getMessage(), exception.getData());
    }

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public RestRsp<Object> handleParameterException(BindException exception) {
        String errorMsg = "参数无效," + exception.getMessage();
        log.error(errorMsg);
        return ofMessage(ErrorCode.PARAM_INVALID, errorMsg);
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    public RestRsp<Object> handleAllException(HttpServletRequest request, Throwable exception) {
        String errorMsg = "系统异常";
        log.error(errorMsg + exception.getMessage(), exception);
        return ofMessage(ErrorCode.SERVER_ERROR, errorMsg);
    }

    public RestRsp<Object> ofMessage(int status, String msg) {
        return ofMessage(status, msg, null);
    }

    public RestRsp<Object> ofMessage(int status, String msg, Object data) {
        return new RestRsp<>(status, msg, data);
    }
}
