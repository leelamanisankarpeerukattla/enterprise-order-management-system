package com.example.payment.kafka;

import com.example.common.events.PaymentRequestedEvent;
import com.example.payment.service.PaymentProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {
    private final PaymentProcessor processor;
    public PaymentEventConsumer(PaymentProcessor processor) { this.processor = processor; }

    @KafkaListener(
        topics = "${app.topics.payments}",
        groupId = "payment-service",
        containerFactory = "paymentRequestedKafkaListenerContainerFactory"
    )
    public void onPaymentRequested(PaymentRequestedEvent ev) {
        processor.process(ev);
    }
}
