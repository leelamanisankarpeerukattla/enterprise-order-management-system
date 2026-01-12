package com.example.common.events;

public class InventoryReservedEvent extends BaseEvent {
    private String orderId;

    public InventoryReservedEvent() {}
    public InventoryReservedEvent(String orderId, String correlationId) {
        super(EventType.INVENTORY_RESERVED, correlationId);
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
