package com.example.order.api;

import com.example.order.web.CorrelationIdFilter;
import com.example.order.api.dto.OrderDtos;
import com.example.order.security.UserContext;
import com.example.order.service.OrderAppService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    private final OrderAppService orders;
    private final UserContext user;

    public OrderController(OrderAppService orders, UserContext user) {
        this.orders = orders;
        this.user = user;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> create(@RequestHeader HttpHeaders headers, @Valid @RequestBody OrderDtos.CreateOrderRequest req) {
        UUID userId = user.userId(headers);
        String corr = headers.getFirst(CorrelationIdFilter.HEADER);
        UUID orderId = orders.createOrder(userId, req.items(), corr);
        return ResponseEntity.ok(java.util.Map.of("orderId", orderId.toString()));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDtos.OrderResponse> get(@RequestHeader HttpHeaders headers, @PathVariable("id") String id) {
        UUID userId = user.userId(headers);
        return ResponseEntity.ok(orders.getOrder(UUID.fromString(id), userId));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> list(@RequestHeader HttpHeaders headers) {
        UUID userId = user.userId(headers);
        return ResponseEntity.ok(orders.listOrders(userId));
    }

    @GetMapping("/admin/orders")
    public ResponseEntity<List<OrderDtos.OrderResponse>> adminList(@RequestHeader HttpHeaders headers) {
        if (!user.roles(headers).contains("ADMIN")) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(orders.adminListAll());
    }
}
