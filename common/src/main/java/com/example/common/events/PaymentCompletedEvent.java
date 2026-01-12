package com.example.common.events;

public class PaymentCompletedEvent extends BaseEvent {
    private String orderId;

    public PaymentCompletedEvent() {}
    public PaymentCompletedEvent(String orderId, String correlationId) {
        super(EventType.PAYMENT_COMPLETED, correlationId);
        this.orderId = orderId;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
