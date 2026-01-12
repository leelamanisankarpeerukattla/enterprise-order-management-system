package com.example.common.events;

import java.util.List;

public class OrderCreatedEvent extends BaseEvent {
    private String orderId;
    private String userId;
    private List<OrderItem> items;

    public OrderCreatedEvent() {}
    public OrderCreatedEvent(String orderId, String userId, List<OrderItem> items, String correlationId) {
        super(EventType.ORDER_CREATED, correlationId);
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
    }

    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public List<OrderItem> getItems() { return items; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public static class OrderItem {
        private String productId;
        private int quantity;

        public OrderItem() {}
        public OrderItem(String productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getProductId() { return productId; }
        public int getQuantity() { return quantity; }
        public void setProductId(String productId) { this.productId = productId; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
