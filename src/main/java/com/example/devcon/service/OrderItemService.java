package com.example.devcon.service;

import com.example.devcon.dto.OrderItemDto;
import com.example.devcon.model.OrderItem;
import com.example.devcon.model.User;
import com.example.devcon.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public static OrderItemDto mapToDto(OrderItem orderItem) {
        if (orderItem != null) {
            return new OrderItemDto(
                    orderItem.getId(),
                    orderItem.getQuantity(),
                    ProductService.mapToDto(orderItem.getProduct()),
                    orderItem.getOrder().getId()
            );
        }
        return null;
    }


    public void delete(User user, Long id) {
        OrderItem item = this.orderItemRepository.findById(id).orElseThrow();
        if (item.getOrder().getUser().getId() != user.getId()) {
            throw new IllegalStateException("Operation not allowed!");
        }
        this.orderItemRepository.deleteById(id);
    }
}
