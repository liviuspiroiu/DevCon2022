package com.example.devcon.ui.controllers;

import com.example.devcon.common.domain.User;
import com.example.devcon.orders.OrderService;
import com.example.devcon.payments.PaymentService;
import com.example.devcon.common.dto.OrderDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;



    public OrderController(final OrderService orderService,
                           final PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/")
    public String list(@AuthenticationPrincipal User user, ModelMap model) {
        final List<OrderDto> orders = orderService.findAllByUser(user);
        model.addAttribute("orders", orders);
        return "/orders/list";
    }

    @GetMapping(value = "/cart")
    public String cart(@AuthenticationPrincipal User user, ModelMap model) {
        final OrderDto order = orderService.findCurrentOrder(user);
        model.addAttribute("order", order);
        return "/orders/cart";
    }

    @GetMapping(value = "/checkout/{orderId}")
    public String checkout(@AuthenticationPrincipal User user, @PathVariable long orderId) {
        paymentService.create(user, orderId);
        return "redirect:/orders/";
    }

    @GetMapping(value = "/increaseQuantity/{orderId}/{orderItemId}")
    public String increaseQuantity(@AuthenticationPrincipal User user,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        orderService.modifyQuantity(user, orderId, orderItemId, 1L);
        return "redirect:/orders/cart";
    }

    @GetMapping(value = "/decreaseQuantity/{orderId}/{orderItemId}")
    public String decreaseQuantity(@AuthenticationPrincipal User user,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        orderService.modifyQuantity(user, orderId, orderItemId, -1L);
        return "redirect:/orders/cart";
    }

    @GetMapping(value = "/deleteItem/{id}")
    public String deleteItem(@AuthenticationPrincipal User user,
                             @PathVariable long id) {
        orderService.deleteItem(user, id);
        return "redirect:/orders/cart";
    }
}
