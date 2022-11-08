package com.example.devcon.orders.model;

import com.example.devcon.common.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByStatusAndUserId(OrderStatus status, Long id);
    List<Order> findAllByUserId(Long id);
}
