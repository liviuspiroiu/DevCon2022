package com.example.devcon.payments.service;

import com.example.devcon.common.enums.PaymentStatus;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentProcessingApi {

    public PaymentProcessingApiResult mockPay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new PaymentProcessingApiResult();
    }

    @Getter
    public static class PaymentProcessingApiResult {
        private final String reference = UUID.randomUUID().toString();
        private final String status = PaymentStatus.ACCEPTED.name();
    }
}
