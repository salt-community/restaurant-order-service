package com.example.restaurantorderservice.dto.kafka;

import com.example.restaurantorderservice.enums.OrderStatus;
import com.example.restaurantorderservice.model.Order;

import java.util.UUID;

public record KafkaOrderDto(UUID orderId, String status) {
    public static KafkaOrderDto fromOrder(Order order) {
        return new KafkaOrderDto(order.getOrderId(), order.getOrderStatus().toString());
    }

    public Order toOrder() {
        return new Order(orderId, OrderStatus.valueOf(status));
    }
}
