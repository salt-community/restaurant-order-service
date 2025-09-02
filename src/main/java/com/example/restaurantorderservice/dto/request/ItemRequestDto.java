package com.example.restaurantorderservice.dto.request;

import com.example.restaurantorderservice.model.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemRequestDto(
    @NotNull @NotBlank String name,
    @NotNull @Min(1) int quantity,
    @NotNull @Min(0) double price
) {
    public Item toItem() {
        Item item = new Item();
        item.setName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        return item;
    }
}
