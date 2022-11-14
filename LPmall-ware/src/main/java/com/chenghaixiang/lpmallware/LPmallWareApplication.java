package com.chenghaixiang.lpmallware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.chenghaixiang.lpmallware.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class LPmallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(LPmallWareApplication.class, args);
    }

}
