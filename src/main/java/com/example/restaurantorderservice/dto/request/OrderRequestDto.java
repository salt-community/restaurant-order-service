package com.example.restaurantorderservice.dto.request;

import com.example.restaurantorderservice.enums.OrderStatus;
import com.example.restaurantorderservice.model.Order;

import java.util.UUID;

public record OrderRequestDto(
    UUID orderId,
    OrderStatus status
) {

    public Order toOrder() {
        Order order = new Order();
        order.setOrderStatus(status);
        return order;
    }

}
