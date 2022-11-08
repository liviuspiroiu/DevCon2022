package com.example.devcon.orders.model;

import com.example.devcon.common.domain.AbstractEntity;
import com.example.devcon.common.domain.Address;
import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.common.dto.OrderItemDto;
import com.example.devcon.common.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends AbstractEntity {

    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "shipped")
    private ZonedDateTime shipped;

    @Column(name = "payment_id")
    private Long paymentId;

    @Embedded
    private Address shipmentAddress;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<OrderItem> orderItems = new HashSet<>();

    private Long userId;
    private String firstName;
    private String lastName;

    public Order(@NotNull BigDecimal totalPrice, @NotNull OrderStatus status,
                 ZonedDateTime shipped, Long paymentId, Address shipmentAddress,
                 Set<OrderItem> orderItems, Long userId, String firstName, String lastName) {
        this.totalPrice = totalPrice;
        this.status = status;
        this.shipped = shipped;
        this.paymentId = paymentId;
        this.shipmentAddress = shipmentAddress;
        this.orderItems = orderItems;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(totalPrice, order.totalPrice) &&
                status == order.status &&
                Objects.equals(shipped, order.shipped) &&
                Objects.equals(paymentId, order.paymentId) &&
                Objects.equals(shipmentAddress, order.shipmentAddress) &&
                Objects.equals(orderItems, order.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPrice, status, shipped, paymentId, shipmentAddress);
    }

    @Override
    public String toString() {
        return "Order{" +
                "totalPrice=" + totalPrice +
                ", status=" + status +
                ", shipped=" + shipped +
                ", paymentId=" + paymentId +
                ", shipmentAddress=" + shipmentAddress +
                '}';
    }

    public OrderDto mapToDto() {

        Set<OrderItemDto> orderItems = this
                .getOrderItems()
                .stream()
                .map(OrderItem::mapToDto)
                .collect(Collectors.toSet());

        return new OrderDto(
                this.getId(),
                this.getTotalPrice(),
                this.getStatus().name(),
                this.getShipped(),
                this.getPaymentId() != null ? this.getPaymentId() : null,
                this.getShipmentAddress().mapToDto(),
                orderItems,
                this.getFirstName() + " " + this.getLastName()
        );
    }
}
