package com.example.common.events;

public class PaymentRequestedEvent extends BaseEvent {
    private String orderId;
    private long amountCents;

    public PaymentRequestedEvent() {}
    public PaymentRequestedEvent(String orderId, long amountCents, String correlationId) {
        super(EventType.PAYMENT_REQUESTED, correlationId);
        this.orderId = orderId;
        this.amountCents = amountCents;
    }

    public String getOrderId() { return orderId; }
    public long getAmountCents() { return amountCents; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setAmountCents(long amountCents) { this.amountCents = amountCents; }
}
