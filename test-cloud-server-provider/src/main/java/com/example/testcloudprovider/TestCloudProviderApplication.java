package com.example.testcloudprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableEurekaClient //表明这是一个eureka客户端
public class TestCloudProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCloudProviderApplication.class, args);
    }

}
