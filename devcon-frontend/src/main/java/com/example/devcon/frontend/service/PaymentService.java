package com.example.devcon.frontend.service;

import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "devcon-payments", configuration = OAuthFeignConfig.class)
public interface PaymentService {
    @RequestMapping(method = RequestMethod.POST, value = "/payments/{id}")
    void create(@PathVariable("id") long orderId);
}
