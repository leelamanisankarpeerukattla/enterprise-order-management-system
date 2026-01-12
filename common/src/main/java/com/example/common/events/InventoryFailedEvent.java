package com.example.common.events;

public class InventoryFailedEvent extends BaseEvent {
    private String orderId;
    private String reason;

    public InventoryFailedEvent() {}
    public InventoryFailedEvent(String orderId, String reason, String correlationId) {
        super(EventType.INVENTORY_FAILED, correlationId);
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setReason(String reason) { this.reason = reason; }
}
