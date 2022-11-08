package com.example.devcondiscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DevconDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevconDiscoveryApplication.class, args);
	}

}
