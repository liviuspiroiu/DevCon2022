package com.example.devcon.frontend.service;

import com.example.devcon.common.dto.CategoryDto;
import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "category-feign-client", url = "http://127.0.0.1:8082/categories", configuration = OAuthFeignConfig.class)
public interface CategoryService {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    List<CategoryDto> findAll();
}
