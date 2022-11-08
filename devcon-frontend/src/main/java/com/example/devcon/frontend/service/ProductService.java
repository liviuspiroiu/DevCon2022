package com.example.devcon.frontend.service;

import com.example.devcon.common.dto.CategoryDto;
import com.example.devcon.common.dto.ProductDto;
import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "devcon-products", configuration = OAuthFeignConfig.class)
public interface ProductService {

    @RequestMapping(method = RequestMethod.GET, value = "/categories/")
    List<CategoryDto> findAllCategories();

    @RequestMapping(method = RequestMethod.GET, value = "/products/")
    List<ProductDto> findAll();

    @RequestMapping(method = RequestMethod.POST, value = "/products/")
    void create(ProductDto product);
}
