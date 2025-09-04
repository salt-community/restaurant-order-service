package com.example.restaurantorderservice.http.dto.response;

import com.example.restaurantorderservice.domain.model.Item;

public record ItemResponseDto(String name, int quantity, double price) {

    public static ItemResponseDto from(Item item) {
        return new ItemResponseDto(item.getName(), item.getQuantity(), item.getPrice());
    }
}
