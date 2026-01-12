package com.example.inventory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product {
    @Id
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "price_cents", nullable = false)
    private long priceCents;

    @Column(nullable = false)
    private int stock;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Product() {}
    public Product(UUID id, String name, long priceCents, int stock) {
        this.id = id; this.name = name; this.priceCents = priceCents; this.stock = stock;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public long getPriceCents() { return priceCents; }
    public int getStock() { return stock; }

    public void setId(UUID id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPriceCents(long priceCents) { this.priceCents = priceCents; }
    public void setStock(int stock) { this.stock = stock; }
}
