package com.example.inventory.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProductDtos {
    public record ProductResponse(String id, String name, long priceCents, int stock) {}
    public record CreateProductRequest(@NotBlank String name, @Min(0) long priceCents, @Min(0) int stock) {}
    public record UpdateStockRequest(@Min(0) int stock) {}
}
