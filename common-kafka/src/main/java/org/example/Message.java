package org.example;

public class Message<T> {

    private final T payload;
    private final CorrelationId id;


    public T getPayload() {
        return payload;
    }

    public CorrelationId getId() {
        return id;
    }

    Message(CorrelationId id, T payload){
        this.id = id;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "payload=" + payload +
                ", id=" + id +
                '}';
    }
}
