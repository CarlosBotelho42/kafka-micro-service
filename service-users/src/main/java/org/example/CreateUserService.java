package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.consumer.ConsumerService;
import org.example.consumer.ServiceRunner;
import org.example.org.example.database.LocalDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

public class CreateUserService implements ConsumerService<Order> {

    private final LocalDatabase database;

    CreateUserService() throws SQLException {
        this.database = new LocalDatabase("users_database");
        this.database.createIfNosExists("create table Users (" +
                "uuid varchar(200) primary key," +
                "email varchar(200))");
    }

    public static void main(String[] args){
        new ServiceRunner<>(CreateUserService::new).start(1);
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        System.out.println("------------------------------------------");
        System.out.println("Processing new order, checking for new user");
        System.out.println(record.value());
        var order = record.value().getPayload();
        if(isNewUser(order.getEmail())) {
            insertNewUser(order.getEmail());
        }
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return CreateUserService.class.getSimpleName();
    }

    private void insertNewUser(String email) throws SQLException {
        var uuid = UUID.randomUUID().toString();
        database.update("insert into Users (uuid, email) " +
                "values (?,?)", uuid, email);
        System.out.println("Usuário" + uuid + "e" + email + " adicionado");
    }

    private boolean isNewUser(String email) throws SQLException {
        var results = database.query("select uuid from Users " +
                "where email = ? limit 1");
        return !results.next();
    }
}