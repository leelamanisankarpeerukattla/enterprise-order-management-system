package com.example.inventory.kafka;

import com.example.common.events.OrderCreatedEvent;
import com.example.inventory.service.InventoryWorkflow;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {
    private final InventoryWorkflow workflow;
    public OrderEventConsumer(InventoryWorkflow workflow) { this.workflow = workflow; }

    @KafkaListener(
        topics = "${app.topics.orders}",
        groupId = "inventory-service",
        containerFactory = "orderCreatedKafkaListenerContainerFactory"
    )
    public void onOrderCreated(OrderCreatedEvent ev) {
        workflow.reserve(ev);
    }
}
