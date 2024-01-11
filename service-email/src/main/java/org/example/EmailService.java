package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.consumer.ConsumerService;
import org.example.consumer.KafkaService;
import org.example.consumer.ServiceProvider;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmailService implements ConsumerService<String> {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
     new ServiceProvider().run(EmailService::new);
    }

    public String getConsumerGroup(){
        return EmailService.class.getSimpleName();
    }

    public String getTopic(){
        return "ECOMMERCE_SEND_EMAIL";
    }

    public void parse(ConsumerRecord<String, Message<String>> record) {
        System.out.println("------------------------------------------");
        System.out.println("Send email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }
}
