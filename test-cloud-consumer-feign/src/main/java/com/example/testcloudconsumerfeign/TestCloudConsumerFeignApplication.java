package com.example.testcloudconsumerfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient //表明这是一个eureka客户端
@EnableFeignClients(basePackages = "com.example.testcloudconsumerfeign.*") //开启feign
public class TestCloudConsumerFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestCloudConsumerFeignApplication.class, args);
    }

}
