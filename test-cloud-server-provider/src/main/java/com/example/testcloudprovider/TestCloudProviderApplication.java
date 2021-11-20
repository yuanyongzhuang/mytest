package com.example.testcloudprovider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = "com.example.testcloudprovider")
@EnableAsync
@EnableEurekaClient //表明这是一个eureka客户端
@MapperScan("com.example.testcloudprovider.mapper")
public class TestCloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCloudProviderApplication.class, args);
    }

}
