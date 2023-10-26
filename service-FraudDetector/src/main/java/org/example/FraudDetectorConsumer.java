package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorConsumer {
    public static void main(String[] args) {
        var fraudDetectorConsumer = new FraudDetectorConsumer();
       try (var service = new KafkaService<>(FraudDetectorConsumer.class.getSimpleName(),
               "ECOMMERCE_NEW_ORDER",
               fraudDetectorConsumer::parse,
               Order.class,
               Map.of())){
        service.run();
    }}

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("Processando novo pedido, checando fraude...");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var order = record.value();

        if(isFraud(order)){
            //simulando fraude qunado o valor for maisr que 4500
            System.out.println("Pedido é uma fraude!!");
            kafkaDispatcher.send("ECOMMERCE_FRAUD_ORDER", order.getUserId(), order);

        }else {
            System.out.println("Pedido aprovado.");
            kafkaDispatcher.send("ECOMMERCE_SUCCESS_ORDER",order.getUserId(), order);

        }
    }
    private static boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
