package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.consumer.KafkaService;
import org.example.dispacher.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailNewOrderService {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var fraudService = new EmailNewOrderService();
        try (var service = new KafkaService<>(EmailNewOrderService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Map.of())) {
            service.run();
        }
    }

    private final KafkaDispatcher<String> emailDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {
        System.out.println("Processando novo pedido, checando email...");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var message = record.value();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }
        var order = message.getPayload();

        var emailCode = "Obrigado pelo seu pedido! Estamos processando se email!";
        var orderMessage = record.value();
        String email = orderMessage.getPayload().getEmail();
        var id = orderMessage.getId().continueWith(EmailNewOrderService.class.getSimpleName());

        emailDispatcher.send("ECOMMERCE_SEND_EMAIL",
                email,
                id,
                emailCode);
    }

    private static boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
