package com.example.payment.service;

import com.example.common.events.PaymentCompletedEvent;
import com.example.common.events.PaymentFailedEvent;
import com.example.common.events.PaymentRequestedEvent;
import com.example.payment.domain.Payment;
import com.example.payment.repo.PaymentRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.UUID;

@Component
public class PaymentProcessor {
    private final PaymentRepo repo;
    private final KafkaTemplate<String, Object> kafka;
    private final String paymentsTopic;
    private final Random rng = new Random();

    public PaymentProcessor(PaymentRepo repo, KafkaTemplate<String, Object> kafka,
                            @Value("${app.topics.payments}") String paymentsTopic) {
        this.repo = repo;
        this.kafka = kafka;
        this.paymentsTopic = paymentsTopic;
    }

    @Transactional
    public void process(PaymentRequestedEvent ev) {
        boolean ok = rng.nextInt(100) >= 5; // 95% success
        repo.save(new Payment(UUID.randomUUID(), UUID.fromString(ev.getOrderId()), ev.getAmountCents(), ok ? "COMPLETED" : "FAILED"));

        if (ok) kafka.send(paymentsTopic, ev.getOrderId(), new PaymentCompletedEvent(ev.getOrderId(), ev.getCorrelationId()));
        else kafka.send(paymentsTopic, ev.getOrderId(), new PaymentFailedEvent(ev.getOrderId(), "Card declined", ev.getCorrelationId()));
    }
}
