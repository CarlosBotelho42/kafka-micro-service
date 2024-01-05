package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.protocol.Message;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;

public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Message.class, new MessageAdapter()).create();

    @Override
    public byte[] serialize(String s, T object) {
        return gson.toJson(object).getBytes();
    }

}
