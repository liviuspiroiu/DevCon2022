package com.example.devcon.model.service;

import com.example.devcon.model.domain.*;
import com.example.devcon.web.dto.OrderDto;
import com.example.devcon.web.dto.OrderItemDto;
import com.example.devcon.model.domain.enumeration.OrderStatus;
import com.example.devcon.model.repository.OrderItemRepository;
import com.example.devcon.model.repository.OrderRepository;
import com.example.devcon.model.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
                    order.getPayment() != null ? order.getPayment().getId() : null,
                    AddressService.mapToDto(order.getShipmentAddress()),
                    orderItems,
                    order.getUser().getFirstName() + " " + order.getUser().getLastName()
            );
        }
        return null;
    }

    public OrderDto create(OrderDto orderDto, User user) {
        log.debug("Request to create Order : {}", orderDto);
        return mapToDto(
                this.orderRepository.save(
                        new Order(
                                BigDecimal.ZERO,
                                OrderStatus.NEW,
                                null,
                                null,
                                AddressService.createFromDto(orderDto.getShipmentAddress()),
                                Collections.emptySet(),
                                user
                        )
                )
        );
    }


    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        this.orderRepository.deleteById(id);
    }

    public OrderDto findCurrentOrder(User user) {
        Optional<Order> order = orderRepository
                .findAllByStatusAndUser_Id(OrderStatus.NEW, user.getId())
                .stream()
                .min(Comparator.comparing(AbstractEntity::getCreatedDate));

        if (order.isPresent()) {
            return mapToDto(order.get());
        }

        return mapToDto(create(user));
    }

    public void addProductToOrder(User user, long productId) {
        Optional<Order> orderOptional = orderRepository
                .findAllByStatusAndUser_Id(OrderStatus.NEW, user.getId())
                .stream()
                .min(Comparator.comparing(AbstractEntity::getCreatedDate));

        Order order = orderOptional.orElseGet(() -> create(user));

        final Product product = productRepository.getReferenceById(productId);

        final OrderItem orderItem = order.getOrderItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(new OrderItem(0L, product, order));

        orderItem.setQuantity(orderItem.getQuantity() + 1);
        orderItemRepository.save(orderItem);

        order.setTotalPrice(
                order.getTotalPrice().add(orderItem.getProduct().getPrice())
        );

        orderRepository.save(order);
    }

    private Order create(User user) {
        return this.orderRepository.save(
                new Order(
                        BigDecimal.ZERO,
                        OrderStatus.NEW,
                        null,
                        null,
                        user.getAddress(),
                        Collections.emptySet(),
                        user
                )
        );
    }

    public void modifyQuantity(User user, long orderId, long orderItemId, long value) {
        final Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getUser().getId() != user.getId()) {
            throw new IllegalStateException("Operation not allowed!");
        }
        final OrderItem orderItem = order.getOrderItems().stream().filter(item -> item.getId() == orderItemId).findFirst().orElseThrow();
        orderItem.updateQuantity(value);
        if (orderItem.getQuantity() == 0) {
            orderItemRepository.deleteById(orderItemId);
        }

        order.setTotalPrice(
                order.getTotalPrice().add(orderItem.getProduct().getPrice().multiply(BigDecimal.valueOf(value)))
        );

        orderRepository.save(order);
    }

    public List<Order> findAllByUser(User user) {
        return orderRepository
                .findAllByUserId(user.getId()).stream()
                .filter(order -> order.getStatus() != OrderStatus.NEW)
                .collect(Collectors.toList());
    }
}