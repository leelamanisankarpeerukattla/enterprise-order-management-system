package com.example.inventory.api;

import com.example.inventory.api.dto.ProductDtos;
import com.example.inventory.domain.Product;
import com.example.inventory.repo.ProductRepo;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {
    private final ProductRepo repo;
    public ProductController(ProductRepo repo) { this.repo = repo; }

    @GetMapping("/products")
    public List<ProductDtos.ProductResponse> list() {
        return repo.findAll().stream()
                .map(p -> new ProductDtos.ProductResponse(p.getId().toString(), p.getName(), p.getPriceCents(), p.getStock()))
                .toList();
    }

    @PostMapping("/admin/products")
    public ResponseEntity<?> create(@RequestHeader HttpHeaders headers, @Valid @RequestBody ProductDtos.CreateProductRequest req) {
        if (!isAdmin(headers)) return ResponseEntity.status(403).build();
        Product p = new Product(UUID.randomUUID(), req.name(), req.priceCents(), req.stock());
        repo.save(p);
        return ResponseEntity.ok(new ProductDtos.ProductResponse(p.getId().toString(), p.getName(), p.getPriceCents(), p.getStock()));
    }

    @PutMapping("/admin/products/{id}/stock")
    public ResponseEntity<?> setStock(@RequestHeader HttpHeaders headers, @PathVariable String id, @Valid @RequestBody ProductDtos.UpdateStockRequest req) {
        if (!isAdmin(headers)) return ResponseEntity.status(403).build();
        Product p = repo.findById(UUID.fromString(id)).orElseThrow();
        p.setStock(req.stock());
        repo.save(p);
        return ResponseEntity.ok().build();
    }

    private boolean isAdmin(HttpHeaders headers) {
        String roles = headers.getFirst("X-User-Roles");
        return roles != null && roles.contains("ADMIN");
    }
}
