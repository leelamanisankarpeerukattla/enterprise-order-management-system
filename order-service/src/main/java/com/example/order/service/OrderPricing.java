package com.example.order.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class OrderPricing {
    private final Map<UUID, Long> priceByProduct = Map.of(
            UUID.fromString("11111111-1111-1111-1111-111111111111"), 1999L,
            UUID.fromString("22222222-2222-2222-2222-222222222222"), 4999L,
            UUID.fromString("33333333-3333-3333-3333-333333333333"), 1299L
    );

    public long priceCents(UUID productId) {
        return priceByProduct.getOrDefault(productId, 999L);
    }
}
