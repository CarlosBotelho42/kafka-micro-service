package org.example.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Message;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ConsumerService<T> {

     void parse(ConsumerRecord<String, Message<T>> record) throws Exception;

     String getTopic();

     String getConsumerGroup();
}
