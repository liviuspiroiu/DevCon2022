package com.example.devcon.service;

import com.example.devcon.dto.OrderDto;
import com.example.devcon.dto.OrderItemDto;
import com.example.devcon.dto.ProductDto;
import com.example.devcon.model.*;
import com.example.devcon.model.enumeration.OrderStatus;
import com.example.devcon.repository.OrderItemRepository;
import com.example.devcon.repository.OrderRepository;
import com.example.devcon.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public static OrderDto mapToDto(Order order) {
        if (order != null) {

            Set<OrderItemDto> orderItems = order
                    .getOrderItems()
                    .stream()
                    .map(OrderItemService::mapToDto)
                    .collect(Collectors.toSet());

            return new OrderDto(
                    order.getId(),
                    order.getTotalPrice(),
                    order.getStatus().name(),
                    order.getShipped(),
                    order.getPayment().getId(),
                    AddressService.mapToDto(order.getShipmentAddress()),
                    orderItems
            );
        }
        return null;
    }

    public List<OrderDto> findAll() {
        log.debug("Request to get all Orders");
        return this.orderRepository.findAll()
                .stream()
                .map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        log.debug("Request to get Order : {}", id);
        return this.orderRepository.findById(id).map(OrderService::mapToDto).orElse(null);
    }

    public List<OrderDto> findAllByUser(Long id) {
        return this.orderRepository.findAllByUserId(id)
                .stream()
                .map(OrderService::mapToDto)
                .collect(Collectors.toList());
    }

    public OrderDto create(OrderDto orderDto) {
        log.debug("Request to create Order : {}", orderDto);
        return mapToDto(
                this.orderRepository.save(
                        new Order(
                                BigDecimal.ZERO,
                                OrderStatus.NEW,
                                null,
                                null,
                                AddressService.createFromDto(orderDto.getShipmentAddress()),
                                Collections.emptySet()
                        )
                )
        );
    }


    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        this.orderRepository.deleteById(id);
    }

    public void addProductToOrder(User user, ProductDto productDto) {
        final Order order = orderRepository
                .findAllByStatusAndUserId(OrderStatus.NEW, user.getId())
                .stream()
                .min(Comparator.comparing(AbstractEntity::getCreatedDate))
                .orElse(create(user.getAddress()));

        final Product product = productRepository.getReferenceById(productDto.getId());

        final OrderItem orderItem = new OrderItem(
                1L,
                product,
                order
        );

        orderItemRepository.save(orderItem);
        order.getOrderItems().add(orderItem);

        orderRepository.save(order);

    }

    private Order create(Address address) {
        return this.orderRepository.save(
                new Order(
                        BigDecimal.ZERO,
                        OrderStatus.NEW,
                        null,
                        null,
                        address,
                        Collections.emptySet()
                )
        );
    }
}
