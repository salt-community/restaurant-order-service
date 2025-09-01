package com.example.restaurantorderservice.dto.request;

import com.example.restaurantorderservice.model.Item;

public record ItemRequestDto(
    String name, int quantity, double price
) {
    public Item toItem() {
        Item item = new Item();
        item.setName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        return item;
    }
}
