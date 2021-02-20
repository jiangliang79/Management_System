package com.server.management_system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-17
 */
@RestController
@RequestMapping("api")
public class TestController {

    @GetMapping("test")
    public Map<String, Object> test() {
        Map<String, Object> objectMap = Maps.newHashMap();
        objectMap.put("test", "test");
        return objectMap;
    }
}
