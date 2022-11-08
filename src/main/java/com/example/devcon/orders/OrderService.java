package com.example.devcon.orders;

import com.example.devcon.products.ProductRepository;
import com.example.devcon.common.domain.AbstractEntity;
import com.example.devcon.users.User;
import com.example.devcon.common.dto.OrderDto;
import com.example.devcon.common.dto.OrderItemDto;
import com.example.devcon.products.Product;
import com.example.devcon.users.AddressService;
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

    public List<OrderDto> findAllByUser(User user) {
        return orderRepository
                .findAllByUserId(user.getId()).stream()
                .filter(order -> order.getStatus() != OrderStatus.NEW)
                .map(OrderService::mapToDto).collect(Collectors.toList());
    }

    public void deleteItem(User user, Long id) {
        OrderItem item = this.orderItemRepository.findById(id).orElseThrow();
        final Order order = item.getOrder();

        if (order.getUser().getId() != user.getId()) {
            throw new IllegalStateException("Operation not allowed!");
        }

        order.getOrderItems().remove(item);
        order.setTotalPrice(order.getTotalPrice()
                .subtract(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));

        orderItemRepository.deleteById(id);
        orderRepository.save(order);
    }
}
