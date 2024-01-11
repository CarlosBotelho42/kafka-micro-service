package org.example.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Message;

public interface ConsumerService<T> {

     void parse(ConsumerRecord<String, Message<String>> record);

     String getTopic();

     String getConsumerGroup();
}
