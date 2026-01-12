package com.example.order.kafka;

import com.example.order.service.OrderAppService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderEventConsumers {
    private final OrderAppService orders;

    public OrderEventConsumers(OrderAppService orders) { this.orders = orders; }

    @KafkaListener(topics = "${app.topics.inventory}", groupId = "order-service")
    public void onInventoryEvent(Object event) {
        if (event instanceof Map<?,?> m) {
            String type = String.valueOf(m.get("type"));
            String orderId = String.valueOf(m.get("orderId"));
            String corr = String.valueOf(m.get("correlationId"));

            if ("INVENTORY_RESERVED".equals(type)) orders.markInventoryReserved(UUID.fromString(orderId), corr);
            else if ("INVENTORY_FAILED".equals(type)) orders.markInventoryFailed(UUID.fromString(orderId));
        }
    }

    @KafkaListener(topics = "${app.topics.payments}", groupId = "order-service")
    public void onPaymentEvent(Object event) {
        if (event instanceof Map<?,?> m) {
            String type = String.valueOf(m.get("type"));
            String orderId = String.valueOf(m.get("orderId"));

            if ("PAYMENT_COMPLETED".equals(type)) orders.markPaymentCompleted(UUID.fromString(orderId));
            else if ("PAYMENT_FAILED".equals(type)) orders.markPaymentFailed(UUID.fromString(orderId));
        }
    }
}
