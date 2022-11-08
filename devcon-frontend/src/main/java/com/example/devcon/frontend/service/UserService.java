package com.example.devcon.frontend.service;

import com.example.devcon.common.dto.UserDto;
import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "users-feign-client", url = "http://127.0.0.1:8081/users", configuration = OAuthFeignConfig.class)
public interface UserService {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    List<UserDto> findAll();

    @RequestMapping(method = RequestMethod.GET, value = "/findByUsername/{username}")
    UserDto findByUsername(@PathVariable("username") String username);

    @RequestMapping(method = RequestMethod.POST, value = "/")
    void save(UserDto user);

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    void delete(@PathVariable("id") long id);
}
