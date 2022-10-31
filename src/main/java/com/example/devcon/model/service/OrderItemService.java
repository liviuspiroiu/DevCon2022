package com.example.devcon.model.service;

import com.example.devcon.model.domain.OrderItem;
import com.example.devcon.web.dto.OrderItemDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemService {
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
}
