package com.example.devconapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DevconApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevconApiGatewayApplication.class, args);
    }

}
