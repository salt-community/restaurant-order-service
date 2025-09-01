package com.example.restaurantorderservice.repository;


import com.example.restaurantorderservice.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
}
