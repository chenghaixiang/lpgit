package com.chenghaixiang.lpmallcoupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LPmallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(LPmallCouponApplication.class, args);
    }

}
