package com.example.order.service;

import com.example.common.events.OrderCreatedEvent;
import com.example.common.events.PaymentRequestedEvent;
import com.example.order.api.dto.OrderDtos;
import com.example.order.domain.Order;
import com.example.order.domain.OrderItem;
import com.example.order.domain.OrderStatus;
import com.example.order.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderAppService {
    private final OrderRepo repo;
    private final KafkaTemplate<String, Object> kafka;
    private final OrderPricing pricing;
    private final String ordersTopic;
    private final String paymentsTopic;

    public OrderAppService(OrderRepo repo, KafkaTemplate<String, Object> kafka, OrderPricing pricing,
                           @Value("${app.topics.orders}") String ordersTopic,
                           @Value("${app.topics.payments}") String paymentsTopic) {
        this.repo = repo;
        this.kafka = kafka;
        this.pricing = pricing;
        this.ordersTopic = ordersTopic;
        this.paymentsTopic = paymentsTopic;
    }

    @Transactional
    public UUID createOrder(UUID userId, List<OrderDtos.Item> items, String correlationId) {
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, userId, OrderStatus.CREATED, 0);

        long total = 0;
        for (OrderDtos.Item it : items) {
            UUID productId = UUID.fromString(it.productId());
            long price = pricing.priceCents(productId);
            order.getItems().add(new OrderItem(UUID.randomUUID(), order, productId, it.quantity(), price));
            total += price * it.quantity();
        }
        order.setTotalCents(total);
        repo.save(order);

        var evItems = order.getItems().stream()
                .map(i -> new OrderCreatedEvent.OrderItem(i.getProductId().toString(), i.getQuantity()))
                .toList();
        kafka.send(ordersTopic, orderId.toString(), new OrderCreatedEvent(orderId.toString(), userId.toString(), evItems, correlationId));

        return orderId;
    }

    @Cacheable(cacheNames = "orders", key = "#orderId.toString() + ':' + #userId.toString()")
    public OrderDtos.OrderResponse getOrder(UUID orderId, UUID userId) {
        Order o = repo.findByIdAndUserId(orderId, userId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return toDto(o);
    }

    public List<OrderDtos.OrderResponse> listOrders(UUID userId) {
        return repo.findByUserId(userId).stream().map(this::toDto).toList();
    }

    public List<OrderDtos.OrderResponse> adminListAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void markInventoryReserved(UUID orderId, String correlationId) {
        Order o = repo.findById(orderId).orElseThrow();
        o.setStatus(OrderStatus.INVENTORY_RESERVED);

        o.setStatus(OrderStatus.PAYMENT_REQUESTED);
        kafka.send(paymentsTopic, orderId.toString(), new PaymentRequestedEvent(orderId.toString(), o.getTotalCents(), correlationId));
    }

    @Transactional
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void markInventoryFailed(UUID orderId) {
        Order o = repo.findById(orderId).orElseThrow();
        o.setStatus(OrderStatus.INVENTORY_FAILED);
    }

    @Transactional
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void markPaymentCompleted(UUID orderId) {
        Order o = repo.findById(orderId).orElseThrow();
        o.setStatus(OrderStatus.PAYMENT_COMPLETED);
    }

    @Transactional
    @CacheEvict(cacheNames = "orders", allEntries = true)
    public void markPaymentFailed(UUID orderId) {
        Order o = repo.findById(orderId).orElseThrow();
        o.setStatus(OrderStatus.PAYMENT_FAILED);
    }

    private OrderDtos.OrderResponse toDto(Order o) {
        return new OrderDtos.OrderResponse(
                o.getId().toString(),
                o.getUserId().toString(),
                o.getStatus().name(),
                o.getTotalCents(),
                o.getItems().stream().map(i -> new OrderDtos.OrderItemResponse(
                        i.getProductId().toString(),
                        i.getQuantity(),
                        i.getPriceCents()
                )).toList()
        );
    }
}
