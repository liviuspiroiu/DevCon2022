package com.example.devcon.payments;

import com.example.devcon.orders.Order;
import com.example.devcon.common.domain.User;
import com.example.devcon.orders.OrderStatus;
import com.example.devcon.orders.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentProcessingApi paymentProcessingApi;


    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          PaymentProcessingApi paymentProcessingApi) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.paymentProcessingApi = paymentProcessingApi;
    }

    public void create(User user, long orderId) {
        final PaymentProcessingApi.PaymentProcessingApiResult result = paymentProcessingApi.mockPay();

        Order order =
                this.orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new IllegalStateException("The Order does not exist!"));

        if (order.getUser().getId() != user.getId()) {
            throw new IllegalStateException("Operation not allowed!");
        }

        final PaymentStatus status = PaymentStatus.valueOf(result.getStatus());
        final Payment payment = this.paymentRepository.save(
                new Payment(
                        result.getReference(),
                        status,
                        order
                ));

        order.setPayment(payment);
        order.setStatus(status.equals(PaymentStatus.ACCEPTED) ? OrderStatus.SENT : OrderStatus.RETRY_PAYMENT);
        orderRepository.save(order);
    }
}