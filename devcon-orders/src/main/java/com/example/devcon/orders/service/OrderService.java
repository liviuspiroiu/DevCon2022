package com.example.devcon.orders.service;

import com.example.devcon.common.domain.AbstractEntity;
import com.example.devcon.common.domain.Address;
import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.common.dto.PaymentDto;
import com.example.devcon.common.enums.OrderStatus;
import com.example.devcon.orders.model.Order;
import com.example.devcon.orders.model.OrderItem;
import com.example.devcon.orders.model.OrderItemRepository;
import com.example.devcon.orders.model.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderDto create(OrderDto orderDto, Long userId, String firstName, String lastName) {
        log.debug("Request to create Order : {}", orderDto);
        return
                this.orderRepository.save(
                        new Order(
                                BigDecimal.ZERO,
                                OrderStatus.NEW,
                                null,
                                null,
                                orderDto.getShipmentAddress().createFromDto(),
                                Collections.emptySet(),
                                userId,
                                firstName,
                                lastName
                        )
                ).mapToDto();

    }

    public List<OrderDto> list(long userId) {
        return orderRepository.findAllByUserId(userId).stream().map(Order::mapToDto).collect(Collectors.toList());
    }

    public OrderDto findCurrentOrder(Long userId, String firstName,
                                     String lastName,
                                     Address shippingAddress) {
        Optional<Order> order = orderRepository
                .findAllByStatusAndUserId(OrderStatus.NEW, userId)
                .stream()
                .min(Comparator.comparing(AbstractEntity::getCreatedDate));

        if (order.isPresent()) {
            return order.get().mapToDto();
        }

        return create(userId, firstName, lastName, shippingAddress).mapToDto();
    }

    public void addProductToOrder(Long userId,
                                  long productId,
                                  BigDecimal productPrice,
                                  String firstName,
                                  String lastName,
                                  Address shippingAddress) {
        Optional<Order> orderOptional = orderRepository
                .findAllByStatusAndUserId(OrderStatus.NEW, userId)
                .stream()
                .min(Comparator.comparing(AbstractEntity::getCreatedDate));

        Order order = orderOptional.orElseGet(() -> create(userId, firstName, lastName, shippingAddress));

        final OrderItem orderItem = order.getOrderItems()
                .stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(new OrderItem(0L, productId, productPrice, order));

        orderItem.setQuantity(orderItem.getQuantity() + 1);
        orderItemRepository.save(orderItem);

        order.setTotalPrice(
                order.getTotalPrice().add(orderItem.getProductPrice())
        );

        orderRepository.save(order);
    }

    private Order create(Long userId, String firstName, String lastName, Address shippingAddress) {
        return this.orderRepository.save(
                new Order(
                        BigDecimal.ZERO,
                        OrderStatus.NEW,
                        null,
                        null,
                        shippingAddress,
                        Collections.emptySet(),
                        userId,
                        firstName,
                        lastName
                )
        );
    }

    public void modifyQuantity(long userId, long orderId, long orderItemId, long value) {
        final Order order = orderRepository.findById(orderId).orElseThrow();

        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("Operation not allowed!");
        }

        final OrderItem orderItem = order.getOrderItems().stream().filter(item -> item.getId() == orderItemId).findFirst().orElseThrow();
        orderItem.updateQuantity(value);
        if (orderItem.getQuantity() == 0) {
            orderItemRepository.deleteById(orderItemId);
        }

        order.setTotalPrice(
                order.getTotalPrice().add(orderItem.getProductPrice().multiply(BigDecimal.valueOf(value)))
        );

        orderRepository.save(order);
    }

    public List<OrderDto> findAllByUser(Long userId) {

        return orderRepository
                .findAllByUserId(userId).stream()
                .filter(order -> order.getStatus() != OrderStatus.NEW)
                .map(Order::mapToDto).collect(Collectors.toList());
    }

    public void deleteItem(Long userId, Long id) {
        OrderItem item = this.orderItemRepository.findById(id).orElseThrow();
        final Order order = item.getOrder();

        if (!order.getUserId().equals(userId)) {
            throw new IllegalStateException("Operation not allowed!");
        }

        order.getOrderItems().remove(item);
        order.setTotalPrice(order.getTotalPrice()
                .subtract(item.getProductPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));

        orderItemRepository.deleteById(id);
        orderRepository.save(order);
    }
}
