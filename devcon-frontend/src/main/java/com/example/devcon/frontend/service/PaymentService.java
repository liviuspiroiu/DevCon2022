package com.example.devcon.frontend.service;

import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "payment-feign-client", url = "http://127.0.0.1:8084/payments", configuration = OAuthFeignConfig.class)
public interface PaymentService {
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    void create(@PathVariable("id") long orderId);
}
