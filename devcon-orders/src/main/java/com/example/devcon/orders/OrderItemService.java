package com.example.devcon.orders;

import com.example.devcon.common.dto.OrderItemDto;
import com.example.devcon.products.ProductService;
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
