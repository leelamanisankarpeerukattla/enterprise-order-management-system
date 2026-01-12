package com.example.inventory.service;

import com.example.common.events.InventoryFailedEvent;
import com.example.common.events.InventoryReservedEvent;
import com.example.common.events.OrderCreatedEvent;
import com.example.inventory.domain.Product;
import com.example.inventory.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class InventoryWorkflow {
    private final ProductRepo repo;
    private final KafkaTemplate<String, Object> kafka;
    private final String inventoryTopic;

    public InventoryWorkflow(ProductRepo repo, KafkaTemplate<String, Object> kafka,
                             @Value("${app.topics.inventory}") String inventoryTopic) {
        this.repo = repo;
        this.kafka = kafka;
        this.inventoryTopic = inventoryTopic;
    }

    @Transactional
    public void reserve(OrderCreatedEvent ev) {
        for (OrderCreatedEvent.OrderItem it : ev.getItems()) {
            Product p = repo.findById(UUID.fromString(it.getProductId())).orElse(null);
            if (p == null || p.getStock() < it.getQuantity()) {
                kafka.send(inventoryTopic, ev.getOrderId(), new InventoryFailedEvent(ev.getOrderId(), "Out of stock", ev.getCorrelationId()));
                return;
            }
        }
        for (OrderCreatedEvent.OrderItem it : ev.getItems()) {
            Product p = repo.findById(UUID.fromString(it.getProductId())).orElseThrow();
            p.setStock(p.getStock() - it.getQuantity());
            repo.save(p);
        }
        kafka.send(inventoryTopic, ev.getOrderId(), new InventoryReservedEvent(ev.getOrderId(), ev.getCorrelationId()));
    }
}
