package com.chenghaixiang.lpmallproduct;


import com.chenghaixiang.lpmallproduct.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class LPmallProductApplicationTests {

    @Autowired
    BrandService brandService;



    @Test
    void contextLoads() {
        System.out.println(brandService.count());
    }

    @Test
    public void testUpload() throws FileNotFoundException {

    }
}
