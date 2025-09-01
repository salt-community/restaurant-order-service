package com.example.restaurantorderservice.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query("""
    SELECT e FROM OutboxEvent e
    WHERE e.processed = false
    ORDER BY e.createdAt DESC
    """)
    List<OutboxEvent> findAllUnprocessed();

}
