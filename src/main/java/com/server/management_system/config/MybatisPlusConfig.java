package com.server.management_system.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-13
 */
@ConditionalOnClass(value = {PaginationInterceptor.class})
@Configuration
@MapperScan("com.server.management_system.dao")
@Slf4j
public class MybatisPlusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        log.info("MybatisPlusConfig.paginationInterceptor init");
        return new PaginationInterceptor();
    }
}