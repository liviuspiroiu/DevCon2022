package com.example.devcon.controller;

import com.example.devcon.service.OrderItemService;
import com.example.devcon.dto.OrderItemDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.devcon.common.Web.API;


@RestController
@RequestMapping(API + "/order-items")
public class OrderItemController {

    private final OrderItemService itemService;

    public OrderItemController(OrderItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<OrderItemDto> findAll() {
        return this.itemService.findAll();
    }

    @GetMapping("/{id}")
    public OrderItemDto findById(@PathVariable Long id) {
        return this.itemService.findById(id);
    }

    @PostMapping
    public OrderItemDto create(@RequestBody OrderItemDto orderItemDto) {
        return this.itemService.create(orderItemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.itemService.delete(id);
    }
}
