package com.example.devcon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DevconApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevconApplication.class, args);
    }

}
