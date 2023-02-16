package com.lagou.kafka.demo.deserializer;

import com.lagou.kafka.demo.entity.User;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class UserDeserializer implements Deserializer<User> {
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    public User deserialize(String topic, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);

        buffer.put(data);
        buffer.flip();

        int userId = buffer.getInt();
        int usernameLength = buffer.getInt();

        String username = new String(data,8,usernameLength);



        return new User(userId,username);
    }

    public void close() {

    }
}
