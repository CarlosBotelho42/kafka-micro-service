package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:users_database.db";
        connection = DriverManager.getConnection(url);
        connection.createStatement().execute("create table Users ("
                + "uuid varchar(200) primary key,"
                + "email varchar(200))");
    }


    public static void main(String[] args) throws SQLException {
        var fraudDetectorConsumer = new CreateUserService();
        try (var service = new KafkaService<>(FraudDetectorConsumer.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudDetectorConsumer::parse,
                Order.class,
                Map.of())){
            service.run();
        }}

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("Processando novo usuario...");
        System.out.println(record.value());

    }
}