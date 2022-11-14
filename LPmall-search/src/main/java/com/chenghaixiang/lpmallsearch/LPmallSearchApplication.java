package com.chenghaixiang.lpmallsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@ConfigurationPropertiesScan(basePackages = "com.chenghaixiang.lpmallsearch.config")
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class LPmallSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(LPmallSearchApplication.class, args);
    }

}
