package com.example.devcon.frontend.controllers;

import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.common.dto.UserDto;
import com.example.devcon.frontend.service.OrderService;
import com.example.devcon.frontend.service.PaymentService;
import com.example.devcon.frontend.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private final UserService userService;


    public OrderController(final OrderService orderService,
                           final PaymentService paymentService, UserService userService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String list(ModelMap model) {
        final List<OrderDto> orders = orderService.findAll(1L);
        model.addAttribute("orders", orders);
        return "/orders/list";
    }

    @GetMapping(value = "/cart")
    public String cart(ModelMap model, Authentication authentication) {
        final String username = ((OAuth2AuthenticationToken)authentication).getPrincipal().getAttribute("preferred_username");
        final UserDto currentUser = userService.findByUsername(username);
        final OrderDto order = orderService.findCurrentOrder(currentUser);
        model.addAttribute("order", order);
        return "/orders/cart";
    }

    @GetMapping(value = "/checkout/{orderId}")
    public String checkout( @PathVariable long orderId) {
        paymentService.create(orderId);
        return "redirect:/orders/";
    }

    @GetMapping(value = "/increaseQuantity/{orderId}/{orderItemId}")
    public String increaseQuantity(@PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        orderService.modifyQuantity(orderId, orderItemId, 1L);
        return "redirect:/orders/cart";
    }

    @GetMapping(value = "/decreaseQuantity/{orderId}/{orderItemId}")
    public String decreaseQuantity(@AuthenticationPrincipal UserDto user,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        orderService.modifyQuantity(orderId, orderItemId, -1L);
        return "redirect:/orders/cart";
    }

    @GetMapping(value = "/deleteItem/{id}")
    public String deleteItem(@PathVariable long id) {
        orderService.deleteItem( id);
        return "redirect:/orders/cart";
    }
}
