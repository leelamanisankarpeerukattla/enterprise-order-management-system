package com.example.order.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderDtos {
    public record CreateOrderRequest(@NotEmpty List<Item> items) {}
    public record Item(@NotBlank String productId, @Min(1) int quantity) {}
    public record OrderItemResponse(String productId, int quantity, long priceCents) {}
    public record OrderResponse(String id, String userId, String status, long totalCents, List<OrderItemResponse> items) {}
}
