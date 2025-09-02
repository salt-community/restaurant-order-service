package com.example.restaurantorderservice.dto.response;

import com.example.restaurantorderservice.model.Item;
import com.example.restaurantorderservice.model.Order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
    UUID orderId,
    Instant createdAt,
    List<Item> items,
    double totalPrice
) {
    public static OrderResponseDto fromOrder(
        Order order
    ) {
        return new OrderResponseDto(
            order.getOrderId(),
            order.getCreatedAt(),
            order.getItems(),
            order.getTotalPrice()
        );
    }
}
