package com.example.devcon.model.service;

import com.example.devcon.model.domain.Order;
import com.example.devcon.model.domain.Payment;
import com.example.devcon.model.domain.User;
import com.example.devcon.model.domain.enumeration.OrderStatus;
import com.example.devcon.model.domain.enumeration.PaymentStatus;
import com.example.devcon.model.repository.OrderRepository;
import com.example.devcon.model.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public void create(User user, long orderId, String paymentReference, String paymentStatus) {
        Order order =
                this.orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));

        if (order.getUser().getId() != user.getId()) {
            throw new IllegalStateException("Operation not allowed!");
        }

        final PaymentStatus status = PaymentStatus.valueOf(paymentStatus);
        final Payment payment = this.paymentRepository.save(
                new Payment(
                        paymentReference,
                        status,
                        order
                ));

        order.setPayment(payment);
        order.setStatus(status.equals(PaymentStatus.ACCEPTED) ? OrderStatus.SENT : OrderStatus.RETRY_PAYMENT);
        orderRepository.save(order);
    }
}