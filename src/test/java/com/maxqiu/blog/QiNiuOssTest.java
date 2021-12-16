package com.maxqiu.blog;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.maxqiu.blog.service.QiNiuOssService;

/**
 * @author Max_Qiu
 */
@SpringBootTest
public class QiNiuOssTest {
    @Autowired
    private QiNiuOssService ossService;

    @Test
    void testUpload() {
        boolean b = ossService.uploadFile(new File("C:\\wolf.png"), "wolf.png");
        System.out.println(b);
    }

    @Test
    void testDelete() {
        boolean b = ossService.deleteFile("wolf.png");
        System.out.println(b);
    }
}
