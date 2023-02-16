package com.lagou.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class MyConsumer {

    public static void main(String[] args) {
        Map<String, Object> configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"linux123:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        //消费组
        configs.put(ConsumerConfig.GROUP_ID_CONFIG,"mygrp1");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(configs);
        consumer.subscribe(Arrays.asList("tp_demo_01"));


        while (true){
            ConsumerRecords<String, String> records = consumer.poll(1000);
            records.forEach(new Consumer<ConsumerRecord<String, String>>() {
                @Override
                public void accept(ConsumerRecord<String, String> record) {
                    System.out.println(record);
                }
            });

        }



    }
}
