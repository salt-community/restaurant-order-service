package com.example.restaurantorderservice.http.dto.request;

import com.example.restaurantorderservice.domain.model.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequestDto(
    @NotNull @NotBlank String name,
    @Min(1) int quantity,
    @Min(0) double price
) {
    public Item toItem() {
        Item item = new Item();
        item.setName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        return item;
    }
}
