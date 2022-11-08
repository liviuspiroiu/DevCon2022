package com.example.devcon.payments.model;

import com.example.devcon.common.domain.AbstractEntity;
import com.example.devcon.common.dto.PaymentDto;
import com.example.devcon.common.enums.PaymentStatus;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment extends AbstractEntity {

    @Column(name = "paypal_payment_id")
    private String paypalPaymentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    private long orderId;

    private long userId;

    public Payment(String paypalPaymentId, @NotNull PaymentStatus status, long orderId, long userId) {
        this.paypalPaymentId = paypalPaymentId;
        this.status = status;
        this.orderId = orderId;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(paypalPaymentId, payment.paypalPaymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paypalPaymentId);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paypalPaymentId='" + paypalPaymentId + '\'' +
                ", status=" + status +
                ", order=" + orderId +
                '}';
    }

    public PaymentDto mapToDto() {
        return new PaymentDto(
                this.getId(),
                this.paypalPaymentId,
                this.status.name(),
                this.orderId,
                this.userId
        );
    }
}
