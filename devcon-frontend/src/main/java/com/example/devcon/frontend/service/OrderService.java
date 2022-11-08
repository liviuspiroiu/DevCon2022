package com.example.devcon.frontend.service;

import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.common.dto.UserDto;
import com.example.devcon.frontend.config.OAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "order-feign-client", url = "http://127.0.0.1:8085/orders", configuration = OAuthFeignConfig.class)
public interface OrderService {
    @RequestMapping(method = RequestMethod.GET, value = "/addProductToOrder/{id}")
    void addProductToOrder(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    List<OrderDto> findAll(@PathVariable("id") long id);

    @RequestMapping(method = RequestMethod.POST, value = "/currentOrder")
    OrderDto findCurrentOrder(UserDto userDto);

    @RequestMapping(method = RequestMethod.GET, value = "/modifyQuantity/{orderId}/{orderItemId}/{quantity}")
    void modifyQuantity(@PathVariable("orderId") long orderId, @PathVariable("orderItemId") long orderItemId, @PathVariable("quantity") long quantity);

    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}")
    void deleteItem(@PathVariable("id") long id);
}
