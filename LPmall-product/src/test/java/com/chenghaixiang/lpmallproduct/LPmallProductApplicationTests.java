package com.chenghaixiang.lpmallproduct;


import com.chenghaixiang.lpmallproduct.service.BrandService;
import com.chenghaixiang.lpmallproduct.service.SpuInfoService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@SpringBootTest
class LPmallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    SpuInfoService spuInfoService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        System.out.println(brandService.count());
    }

    @Test
    public void testUpload() throws FileNotFoundException {
    }

    @Test
    public void RedisTest(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello","word1");

        String hello = ops.get("hello");
        System.out.println(hello);
    }
}
