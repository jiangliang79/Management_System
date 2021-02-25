package com.server.management_system;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Maps;

@SpringBootTest
class ManagementSystemApplicationTests {

    @Autowired(required = false)
    private DocumentConverter converter;

    @Test
    void contextLoads() {
        File path = new File(System.getProperty("user.dir") + "/src/main/resources");
        File upload = new File(path.getAbsolutePath(), "/file/");
        File dest = new File(upload.getAbsolutePath() + "/fileName");
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();// 新建文件夹
        }
        System.out.println("ff/fff.xx".substring("ff/fff.xx".lastIndexOf(".")));
        System.out.println(dest.getAbsolutePath());
    }

    @Test
    public Map<String, Object> test() throws OfficeException {
        File file = new File(
                "/Users/jiangliang1/Management_System/src/main/resources/file/信息科学与工程学院_计数1701班_姜良的副本.docx");//需要转换的文件
        File newFile = new File("/Users/jiangliang1/Management_System/src/main/resources/pdf");//转换之后文件生成的地址
        String savePath = newFile.getAbsolutePath() + "/"; //pdf文件生成保存的路径
        String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        String fileType = ".pdf"; //pdf文件后缀
        String newFileMix = savePath + fileName + fileType;  //将这三个拼接起来,就是我们最后生成文件保存的完整访问路径了
        converter.convert(file).to(new File(newFileMix)).execute();
        return Maps.newHashMap();
    }

}
