package com.chenghaixiang.lpmallthirdparty;

import com.aliyun.oss.OSS;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
class LPmallThirdPartyApplicationTests {


    @Autowired
    OSS ossClient;

    @Test
    public void testUpload() throws FileNotFoundException {

        String filePath= "C:\\Users\\86182\\Desktop\\图片\\黑魔导.jpg";

        FileInputStream fileInputStream = new FileInputStream(filePath);

        ossClient.putObject("lpmall-hello","黑魔导2.jpg",fileInputStream);
        ossClient.shutdown();
    }
}
