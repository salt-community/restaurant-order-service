package com.example.restaurantorderservice.messaging.eventDto;

import com.example.restaurantorderservice.domain.model.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record KafkaMessageItemDto(
    @NotNull UUID id,
    @NotNull @NotBlank String name,
    @Min(1) int quantity,
    @Min(0) double price
) {
    public static KafkaMessageItemDto fromItem(Item item) {
        return new KafkaMessageItemDto(
            item.getId(),
            item.getName(),
            item.getQuantity(),
            item.getPrice()
        );
    }
}
