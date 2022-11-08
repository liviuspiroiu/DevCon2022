package com.example.devcon.frontend.service;

import com.example.devcon.common.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "category-feign-client", url = "http://127.0.0.1:8082/categories")
public interface CategoryService {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    CategoryDto findAll();
}
