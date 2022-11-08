package com.example.devcon.payments.controller;

import com.example.devcon.common.dto.PaymentDto;
import com.example.devcon.payments.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Boolean> save(@PathVariable("orderId") long orderId) {
        long userId = 1L;
        paymentService.create(orderId, userId);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/")
    public ResponseEntity<List<PaymentDto>> list() {
        long userId = 1L;
        return ResponseEntity.ok(paymentService.list(userId));
    }
}
