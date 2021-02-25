package com.server.management_system.controller;

import java.io.File;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
public class Test {
    public static void main(String[] args) {
        File path = new File(System.getProperty("user.dir") + "/src/main/resources");
        File upload = new File(path.getAbsolutePath(), "file/");
        File dest = new File(upload.getAbsolutePath() + "/fileName");
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();// 新建文件夹
        }
        System.out.println(dest.getAbsolutePath());
    }
}
