package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailConsumer {
    public static void main(String[] args) {
        var emailConsumer = new EmailConsumer();
        try (var service = new KafkaService<>(EmailConsumer.class.getSimpleName(),
                "ECOMMERCE_SEND_EMAIL",
                emailConsumer::parse,
                String.class,
                Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record) {
        System.out.println("Processando email...");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("email processado.");

    }
}
