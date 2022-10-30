package com.example.devcon.web.controller;

import com.example.devcon.web.dto.OrderDto;
import com.example.devcon.model.domain.Order;
import com.example.devcon.model.domain.User;
import com.example.devcon.model.service.OrderItemService;
import com.example.devcon.model.service.OrderService;
import com.example.devcon.model.service.PaymentProcessingApi;
import com.example.devcon.model.service.PaymentService;
import org.springframework.security.core.Authentication;
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
    private final OrderItemService orderItemService;

    private final PaymentProcessingApi paymentProcessingApi;

    public OrderController(final OrderService orderService,
                           final PaymentService paymentService,
                           final OrderItemService orderItemService,
                           final PaymentProcessingApi paymentProcessingApi) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.orderItemService = orderItemService;
        this.paymentProcessingApi = paymentProcessingApi;
    }

    @GetMapping(value = "/")
    public String list(Authentication authentication, ModelMap model) {
        final User user = (User) authentication.getPrincipal();
        final List<Order> orders = orderService.findAllByUser(user);
        model.addAttribute("orders", orders);
        return "/orders/list";
    }

    @GetMapping(value = "/cart")
    public String cart(Authentication authentication, ModelMap model) {
        final User user = (User) authentication.getPrincipal();
        final OrderDto order = orderService.findCurrentOrder(user);
        model.addAttribute("order", order);
        return "/orders/cart";
    }

    @GetMapping(value = "/checkout/{id}")
    public String checkout(Authentication authentication, @PathVariable long id) {
        final User user = (User) authentication.getPrincipal();
        final PaymentProcessingApi.PaymentProcessingApiResult result = paymentProcessingApi.mockPay();
        paymentService.create(user, id, result.getReference(), result.getStatus());
        return "redirect:/orders/";
    }

    @GetMapping(value = "/increaseQuantity/{orderId}/{orderItemId}")
    public String increaseQuantity(Authentication authentication,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        final User user = (User) authentication.getPrincipal();
        orderService.modifyQuantity(user, orderId, orderItemId, 1L);
        return "redirect:/orders/cart";
    }

    @GetMapping(value = "/decreaseQuantity/{orderId}/{orderItemId}")
    public String decreaseQuantity(Authentication authentication,
                                   @PathVariable long orderId,
                                   @PathVariable long orderItemId) {
        final User user = (User) authentication.getPrincipal();
        orderService.modifyQuantity(user, orderId, orderItemId, -1L);
        return "redirect:/orders/cart";
    }

    @GetMapping(value = "/deleteItem/{id}")
    public String deleteItem(Authentication authentication,
                             @PathVariable long id) {
        final User user = (User) authentication.getPrincipal();
        orderItemService.delete(user, id);
        return "redirect:/orders/cart";
    }
}
