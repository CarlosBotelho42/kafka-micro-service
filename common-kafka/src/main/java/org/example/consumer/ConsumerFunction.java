package org.example.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.Message;

import java.util.concurrent.ExecutionException;

public interface ConsumerFunction<T>  {
    void consume(ConsumerRecord<String, Message<T>> record) throws Exception;
}
