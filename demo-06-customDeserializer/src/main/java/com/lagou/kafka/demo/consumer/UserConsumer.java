package com.lagou.kafka.demo.consumer;

import com.lagou.kafka.demo.deserializer.UserDeserializer;
import com.lagou.kafka.demo.entity.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class UserConsumer {
    public static void main(String[] args) {
        Map<String, Object> configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "linux123:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        //设置自定义化的反序列化器
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserDeserializer.class);

        configs.put(ConsumerConfig.GROUP_ID_CONFIG,"user_consumer");
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG,"consumer_id");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        KafkaConsumer<String, User> consumer = new KafkaConsumer<String, User>(configs);

        //订阅主题
        consumer.subscribe(Collections.singleton("tp_user_01"));

        ConsumerRecords<String, User> records = consumer.poll(Long.MAX_VALUE);

        records.forEach(new Consumer<ConsumerRecord<String, User>>() {
            @Override
            public void accept(ConsumerRecord<String, User> record) {
                System.out.println(record.value());
            }
        });

        //关闭消费者
        consumer.close();

    }
}
