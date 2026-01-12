package com.example.order.security;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class UserContext {
    public UUID userId(HttpHeaders headers) {
        return UUID.fromString(headers.getFirst("X-User-Id"));
    }
    public List<String> roles(HttpHeaders headers) {
        String raw = headers.getFirst("X-User-Roles");
        if (raw == null) return List.of();
        raw = raw.replace("[","").replace("]","");
        if (raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(",")).map(String::trim).toList();
    }
}
