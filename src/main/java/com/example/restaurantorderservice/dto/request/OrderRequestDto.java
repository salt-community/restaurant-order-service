package com.example.restaurantorderservice.dto.request;

import com.example.restaurantorderservice.enums.OrderStatus;
import com.example.restaurantorderservice.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public record OrderRequestDto(
    @Valid @NotEmpty List<ItemRequestDto> items
) {

    public Order toOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.PLACED);
        order.setCreatedAt(Instant.now());
        order.setTotalPrice(
            items.stream()
                .mapToDouble(itemRequestDto -> itemRequestDto.price() * itemRequestDto.quantity())
                .sum()
        );
        order.setItems(items.stream().map(ItemRequestDto::toItem).toList());
        return order;
    }

}
