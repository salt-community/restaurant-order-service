package com.example.restaurantorderservice.messaging;

import com.example.restaurantorderservice.domain.service.OrderService;
import com.example.restaurantorderservice.messaging.consumer.PaymentFailedListener;
import com.example.restaurantorderservice.messaging.eventDto.PaymentFailedDto;
import com.example.restaurantorderservice.messaging.idempotent.ConsumedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Getter
public class PaymentFailedListenerTestExtender extends PaymentFailedListener {

    private CountDownLatch latch = new CountDownLatch(1);

    private PaymentFailedDto payload;

    public PaymentFailedListenerTestExtender(ObjectMapper mapper, OrderService orderService, ConsumedEventRepository consumedEventRepository) {
        super(mapper, orderService, consumedEventRepository);
    }


    @Override
    public void onPaymentFailed(ConsumerRecord<String, String> record) {
        latch.countDown();
        payload = readPaymentFailed(record.value());
    }


    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
