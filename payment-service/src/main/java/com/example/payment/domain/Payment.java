package com.example.payment.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "amount_cents", nullable = false)
    private long amountCents;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Payment() {}
    public Payment(UUID id, UUID orderId, long amountCents, String status) {
        this.id = id; this.orderId = orderId; this.amountCents = amountCents; this.status = status;
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public long getAmountCents() { return amountCents; }
    public String getStatus() { return status; }

    public void setId(UUID id) { this.id = id; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public void setAmountCents(long amountCents) { this.amountCents = amountCents; }
    public void setStatus(String status) { this.status = status; }
}
