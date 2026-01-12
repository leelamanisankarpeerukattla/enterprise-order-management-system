package com.example.inventory.config;

import com.example.inventory.domain.Product;
import com.example.inventory.repo.ProductRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner seed(ProductRepo repo) {
        return args -> {
            saveIfMissing(repo, new Product(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Wireless Mouse", 1999, 50));
            saveIfMissing(repo, new Product(UUID.fromString("22222222-2222-2222-2222-222222222222"), "Mechanical Keyboard", 4999, 30));
            saveIfMissing(repo, new Product(UUID.fromString("33333333-3333-3333-3333-333333333333"), "USB-C Cable", 1299, 100));
        };
    }

    private void saveIfMissing(ProductRepo repo, Product p) {
        if (!repo.existsById(p.getId())) repo.save(p);
    }
}
