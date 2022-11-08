package com.example.devcon.orders.model;

import com.example.devcon.common.domain.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem extends AbstractEntity {

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    private Long productId;

    private BigDecimal productPrice;

    @ManyToOne
    private Order order;

    public OrderItem(@NotNull Long quantity, Long productId, BigDecimal productPrice, Order order) {
        this.quantity = quantity;
        this.productId = productId;
        this.order = order;
        this.productPrice = productPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(quantity, orderItem.quantity) &&
                Objects.equals(productId, orderItem.productId) &&
                Objects.equals(order, orderItem.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, productId, order);
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "quantity=" + quantity +
                ", productId=" + productId +
                '}';
    }

    public void updateQuantity(long value) {
        this.quantity += value;
    }
}
