package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    public CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:users_database.db";
        connection = DriverManager.getConnection(url);

        try {
            connection.createStatement().execute("create table Users ("
                + "uuid varchar(200) primary key,"
                + "email varchar(200))");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        var createUserService = new CreateUserService();
        try (var service = new KafkaService<>(FraudDetectorConsumer.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                createUserService::parse,
                Order.class,
                Map.of())){
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws  SQLException {
        System.out.println("Processando novo usuario...");
        System.out.println(record.value());
        var order = record.value();
        if(isNewUser(order.getEmail())){
            insertNewUser( order.getEmail());
        }
    }

    private void insertNewUser(String email) throws SQLException {
      var insert = connection.prepareStatement("insert into Users (uuid, email)" +
                "values (?, ?)");
      insert.setString(1, UUID.randomUUID().toString());
      insert.setString(2, email);
      insert.execute();
      System.out.println("Usuario uuid e " + email + "adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        var exists = connection.prepareStatement("select uuid from Users " + "where email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        return !results.next();
    }
}