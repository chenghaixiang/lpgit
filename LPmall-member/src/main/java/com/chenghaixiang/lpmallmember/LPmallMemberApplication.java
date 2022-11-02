package com.chenghaixiang.lpmallmember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.chenghaixiang.lpmallmember.feignservice")
@EnableDiscoveryClient
@SpringBootApplication
public class LPmallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(LPmallMemberApplication.class, args);
    }

}
