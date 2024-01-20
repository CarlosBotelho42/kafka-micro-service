package org.example;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.consumer.ConsumerService;
import org.example.consumer.ServiceRunner;
import org.example.dispacher.KafkaDispatcher;
import org.example.org.example.database.LocalDatabase;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService implements ConsumerService<Order> {

    private final LocalDatabase database;

    FraudDetectorService() throws SQLException {
        this.database = new LocalDatabase("frauds_database");
        this.database.createIfNosExists("create table Orders (" +
                "uuid varchar(200) primary key," +
                "is_fraud boolean)");
    }

    public static void main(String[] args){
        new ServiceRunner<>(FraudDetectorService::new).start(1);
    }

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException, SQLException {
        System.out.println("Processando novo pedido, checando fraude...");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());

        var message = record.value();
        var order = message.getPayload();

        if(waProcessed(order)){
            System.out.println("Pedido" + order.getOrderId() + "ja foi processado");
            return;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // ignoring
            e.printStackTrace();
        }


        if(isFraud(order)){
            //inserindo no banco a fraude
            database.update("insert into Orders (uuid,is_fraud) values (?,true)", order.getOrderId());
            //simulando fraude qunado o valor for maisr que 4500
            System.out.println("Pedido é uma fraude!!");
            kafkaDispatcher.send("ECOMMERCE_FRAUD_ORDER", order.getEmail(),
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()),
                    order);

        }else {
            database.update("insert into Orders (uuid,is_fraud) values (?,false)", order.getOrderId());
            System.out.println("Pedido aprovado.");
            kafkaDispatcher.send("ECOMMERCE_SUCCESS_ORDER", order.getEmail(),
                    message.getId().continueWith(FraudDetectorService.class.getSimpleName()),
                    order);

        }
    }

    private boolean waProcessed(Order order) throws SQLException {
       var result = database.query("select uui from Orders where uuid = ? limit 1", order.getOrderId());
       return result.next();
    }

    @Override
    public String getTopic() {
        return "ECOMMERCE_NEW_ORDER";
    }

    @Override
    public String getConsumerGroup() {
        return FraudDetectorService.class.getSimpleName();
    }

    private static boolean isFraud(Order order) {
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }
}
