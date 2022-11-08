package com.example.devcon.frontend.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class OAuthFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        OAuthClientCredentialsFeignManager clientCredentialsFeignManager = new OAuthClientCredentialsFeignManager();
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + clientCredentialsFeignManager.getAccessToken());
        };
    }

}