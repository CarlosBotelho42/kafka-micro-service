package org.example.consumer;

public interface ServiceFactory<T> {
    ConsumerService<T> create();
}
