package com.example.devcon.controller;

import com.example.devcon.dto.OrderDto;
import com.example.devcon.model.User;
import com.example.devcon.service.OrderItemService;
import com.example.devcon.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    public OrderController(OrderService orderService,
                           OrderItemService orderItemService) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
    }

    @GetMapping(value = "/")
    public String findAll(Authentication authentication, ModelMap model) {
        final User user = (User) authentication.getPrincipal();
        final OrderDto order = orderService.findCurrentOrder(user);
        model.addAttribute("order", order);
        return "/orders/list";
    }

    @GetMapping(value = "/increaseQuantity/{orderId}/{orderItemId}")
    public String increaseQuantity(Authentication authentication,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        final User user = (User) authentication.getPrincipal();
        orderService.modifyQuantity(user, orderId, orderItemId, 1L);
        return "redirect:/orders/";
    }

    @GetMapping(value = "/decreaseQuantity/{orderId}/{orderItemId}")
    public String decreaseQuantity(Authentication authentication,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        final User user = (User) authentication.getPrincipal();
        orderService.modifyQuantity(user, orderId, orderItemId, -1L);
        return "redirect:/orders/";
    }

    @GetMapping(value = "/deleteItem/{id}")
    public String deleteItem(Authentication authentication,
                                   @PathVariable long id) {
        final User user = (User) authentication.getPrincipal();
        orderItemService.delete(user, id);
        return "redirect:/orders/";
    }
}
