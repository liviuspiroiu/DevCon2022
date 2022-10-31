package com.example.devcon.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findAllByStatusAndUser_Id(OrderStatus status, Long id);
    List<Order> findAllByUserId(Long id);
}
