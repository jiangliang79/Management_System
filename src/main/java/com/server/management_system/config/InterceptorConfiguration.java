package com.server.management_system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import com.server.management_system.auth.InternalApiAuthInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-13
 */
@Configuration
@Slf4j
public class InterceptorConfiguration implements WebMvcConfigurer {

    @Autowired
    private InternalApiAuthInterceptor internalApiAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(internalApiAuthInterceptor)
                .addPathPatterns("/api/system/management/**").excludePathPatterns("/api/system/management/common/**")
                .excludePathPatterns("/api/system/management/article/preview");
        log.info("InterceptorConfiguration init");
    }
}
