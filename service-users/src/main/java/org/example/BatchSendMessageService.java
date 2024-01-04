package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BatchSendMessageService {

    private final Connection connection;
    public BatchSendMessageService() throws SQLException {
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
        var bathService = new BatchSendMessageService();
        try (var service = new KafkaService<>(BatchSendMessageService.class.getSimpleName(),
                "SEND_MESSAGE_TO_ALL_USERS",
                bathService::parse,
                String.class,
                Map.of())){
            service.run();
        }
    }

    private final KafkaDispatcher<User> kafkaDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Message<String>> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("Processando novo bath...");
        var message = record.value();

        System.out.println("Topic" + message.getPayload());


        for (User user : getAllUsers()) {
            kafkaDispatcher.send(message.getPayload(), user.getUuid(), user);
        }
    }

    private List<User> getAllUsers() throws SQLException {
        var result = connection.prepareStatement("select uuid from Users").executeQuery();
        List<User> users = new ArrayList<>();
        while (result.next()){
            users.add(new User(result.getNString(1)));
        }
        return users;
    }

}
