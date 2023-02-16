package com.lagou.kafka.demo.serializer;

import com.lagou.kafka.demo.entity.User;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Map;

public class UserSerializer implements Serializer<User> {
    public void configure(Map<String, ?> map, boolean b) {
        //do nothing
    }

    public byte[] serialize(String s, User user) {
        try {
            // 如果数据是null，则返回null
            if (user == null) return null;
            Integer userId = user.getUserId();
            String username = user.getUsername();
            int length = 0;
            byte[] bytes = null;
            if (username != null) {
                bytes = username.getBytes("utf-8");
                length = bytes.length;
            }

            //第一个4个字节用于存储userId的值
            //第二个4个字节用于存储username字节数组的长度int值
            //第三个长度，用于存放username序列化之后的字节数组
            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + length);

            //设置userId
            buffer.putInt(userId);
            //设置username字节数组的长度
            buffer.putInt(length);
            //设置username字节数组
            buffer.put(bytes);
            //以字节数组形式返回user对象的值
            return buffer.array();
        } catch (UnsupportedEncodingException e) {
            throw new SerializationException("序列化数据异常");
        }
    }

    public void close() {
        //do nothing
    }
}
