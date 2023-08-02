package com.summer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
@ImportResource(locations = {"classpath:emailInfo.xml"})
@EnableFeignClients(basePackages = {"com.summer.common.feign"})
@MapperScan({"com.summer.mapper", "com.summer.common.mapper", "com.summer.business.mapper"})
public class PlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformApplication.class, args);
    }



}
