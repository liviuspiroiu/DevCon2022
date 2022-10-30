package com.example.devcon.repository;

import com.example.devcon.model.Order;
import com.example.devcon.model.enumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByStatusAndUserId(OrderStatus status, Long id);
    List<Order> findAllByUserId(Long id);
}
