package com.chenghaixiang.lpmallproduct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@MapperScan("com.chenghaixiang.lpmallproduct.dao")
@SpringBootApplication
public class LPmallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(LPmallProductApplication.class, args);
    }

}
