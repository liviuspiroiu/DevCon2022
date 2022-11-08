package com.example.devcon.orders.controller;

import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.common.dto.UserDto;
import com.example.devcon.orders.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ResponseEntity<Boolean> save(@RequestBody OrderDto orderDto, Authentication authentication) {
        long userId = 1L;
        final String firstName = ((OAuth2AuthenticationToken)authentication).getPrincipal().getAttribute("first_name");
        final String lastName = ((OAuth2AuthenticationToken)authentication).getPrincipal().getAttribute("last_name");
        orderService.create(orderDto, userId, firstName, lastName);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDto>> list(@PathVariable("id") long userId) {
        return ResponseEntity.ok(orderService.list(userId));
    }

    @PostMapping("/currentOrder")
    public ResponseEntity<OrderDto> currentOrder(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(orderService.findCurrentOrder(userDto));
    }
}
