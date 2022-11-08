package com.example.devcon.orders.controller;

import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.orders.service.OrderService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Boolean> save(@RequestBody OrderDto orderDto) {
        long userId = 1L;
        String firstName = "";
        String lastName = "";
        orderService.create(orderDto, userId, firstName, lastName);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderDto>> list() {
        long userId = 1L;
        return ResponseEntity.ok(orderService.list(userId));
    }
}
