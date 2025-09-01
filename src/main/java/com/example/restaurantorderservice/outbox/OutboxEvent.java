package com.example.restaurantorderservice.outbox;

import com.example.restaurantorderservice.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "outbox_event")
@Builder
@ToString
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID outboxId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private UUID orderId;

    private String payload;

    private Instant createdAt;

    private boolean processed;

}
