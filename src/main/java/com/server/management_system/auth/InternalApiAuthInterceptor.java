package com.server.management_system.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.server.management_system.constant.ApiConst;
import com.server.management_system.constant.ErrorCode;
import com.server.management_system.exception.ServiceException;
import com.server.management_system.util.TokenUtil;

@Component
public class InternalApiAuthInterceptor extends HandlerInterceptorAdapter {

    private static final String ERROR_MSG = "您没有接口调用权限，请联系管理员添加";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String tokenHeader = request.getHeader(ApiConst.AUTHENTICATION_TOKEN_HEADER_KEY);
        if (TokenUtil.tokenValid(tokenHeader)) {
            return true;
        }
        throw ServiceException.of(ErrorCode.PERMISSION_DENIED, ERROR_MSG);
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable
            ModelAndView modelAndView) throws Exception {
    }
}
