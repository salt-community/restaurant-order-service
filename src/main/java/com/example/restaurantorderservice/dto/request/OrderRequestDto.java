package com.example.restaurantorderservice.dto.request;

import com.example.restaurantorderservice.enums.OrderStatus;
import com.example.restaurantorderservice.model.Item;
import com.example.restaurantorderservice.model.Order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderRequestDto(
    List<ItemRequestDto> items
) {

    public Order toOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PLACED);
        order.setCreatedAt(Instant.now());
        order.setTotalPrice(
            items.stream()
                .mapToDouble(ItemRequestDto::price)
                .sum()
        );
        order.setItems(items.stream().map(ItemRequestDto::toItem).toList());
        return order;
    }

}
