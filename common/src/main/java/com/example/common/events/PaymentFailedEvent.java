package com.example.common.events;

public class PaymentFailedEvent extends BaseEvent {
    private String orderId;
    private String reason;

    public PaymentFailedEvent() {}
    public PaymentFailedEvent(String orderId, String reason, String correlationId) {
        super(EventType.PAYMENT_FAILED, correlationId);
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() { return orderId; }
    public String getReason() { return reason; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setReason(String reason) { this.reason = reason; }
}
