package com.example.devcon.frontend.service;

import com.example.devcon.common.dto.ProductDto;
import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "product-feign-client", url = "http://127.0.0.1:8081/products", configuration = OAuthFeignConfig.class)
public interface ProductService {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    List<ProductDto> findAll();

    @RequestMapping(method = RequestMethod.POST, value = "/")
    void create(ProductDto product);
}
