package com.example.order.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price_cents", nullable = false)
    private long priceCents;

    public OrderItem() {}
    public OrderItem(UUID id, Order order, UUID productId, int quantity, long priceCents) {
        this.id = id; this.order = order; this.productId = productId; this.quantity = quantity; this.priceCents = priceCents;
    }

    public UUID getId() { return id; }
    public Order getOrder() { return order; }
    public UUID getProductId() { return productId; }
    public int getQuantity() { return quantity; }
    public long getPriceCents() { return priceCents; }

    public void setId(UUID id) { this.id = id; }
    public void setOrder(Order order) { this.order = order; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPriceCents(long priceCents) { this.priceCents = priceCents; }
}
