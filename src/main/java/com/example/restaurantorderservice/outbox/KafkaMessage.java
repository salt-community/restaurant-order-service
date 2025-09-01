package com.example.restaurantorderservice.outbox;

import com.example.restaurantorderservice.enums.OrderStatus;
import com.example.restaurantorderservice.model.Item;
import com.example.restaurantorderservice.model.Order;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record KafkaMessage(
    UUID orderId,
    OrderStatus orderStatus,
    Instant createdAt,
    double totalPrice,
    List<Item> items
) {
    public static KafkaMessage fromOrder(Order order) {
        return new KafkaMessage(
            order.getOrderId(),
            order.getOrderStatus(),
            order.getCreatedAt(),
            order.getTotalPrice(),
            order.getItems()
        );
    }
}
