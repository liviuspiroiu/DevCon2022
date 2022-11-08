package com.example.devcon.payments.service;

import com.example.devcon.common.dto.PaymentDto;
import com.example.devcon.common.enums.PaymentStatus;
import com.example.devcon.payments.model.Payment;
import com.example.devcon.payments.model.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentProcessingApi paymentProcessingApi;


    public PaymentService(PaymentRepository paymentRepository,
                          PaymentProcessingApi paymentProcessingApi) {
        this.paymentRepository = paymentRepository;
        this.paymentProcessingApi = paymentProcessingApi;
    }

    public void create(long orderId, long userId) {
        final PaymentProcessingApi.PaymentProcessingApiResult result = paymentProcessingApi.mockPay();

        final PaymentStatus status = PaymentStatus.valueOf(result.getStatus());
        this.paymentRepository.save(
                new Payment(
                        result.getReference(),
                        status,
                        orderId,
                        userId
                ));

    }

    public List<PaymentDto> list(long userId) {
        return paymentRepository.findAllByUserId(userId).stream().map(Payment::mapToDto).collect(Collectors.toList());
    }
}