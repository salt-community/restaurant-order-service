package com.example.restaurantorderservice.outbox;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class OutboxWorker {

    private final OutboxRepository outboxRepository;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 500)
    public void publishOutboxEvents() {

        List<OutboxEvent> outboxEvents = outboxRepository.findAllUnprocessed();

        for (OutboxEvent event : outboxEvents) {
            System.out.println("eveneeeeeeeeeeet = " + event);
            kafkaTemplate.send("order.created", event.getPayload());
            event.setProcessed(true);
            System.out.println("eveneeeeeeeeeeet = " + event);
            outboxRepository.save(event);
        }


    }
}
