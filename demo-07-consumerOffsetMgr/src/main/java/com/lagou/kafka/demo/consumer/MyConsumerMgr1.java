package com.lagou.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.sql.Connection;
import java.util.*;
import java.util.function.BiConsumer;

public class MyConsumerMgr1 {
    public static void main(String[] args) {

        Map<String, Object> configs = new HashMap<String, Object>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"linux123:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        //消费组
        configs.put(ConsumerConfig.GROUP_ID_CONFIG,"mygrp1");


        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(configs);
//        consumer.subscribe(Collections.singleton("tp_demo_01"));

//        Map<String, List<PartitionInfo>> stringListMap =  consumer.listTopics();
//        stringListMap.forEach(new BiConsumer<String, List<PartitionInfo>>() {
//            public void accept(String topicname, List<PartitionInfo> partitionInfos) {
//                System.out.println("主题名称: " + topicname);
//                for (PartitionInfo partitionInfo : partitionInfos) {
//                    System.out.println(partitionInfo);
//                }
//            }
//        });

//        Set<TopicPartition> assignment1 = consumer.assignment();
//
//        for (TopicPartition partition : assignment1) {
//            System.out.println(partition);
//        }
//
//        System.out.println("------------------------");

        //给当前消费者分配指定的主题分区
        consumer.assign(Arrays.asList(
                new TopicPartition("tp_demo_01",0),
                new TopicPartition("tp_demo_01",1),
                new TopicPartition("tp_demo_01",2)
        ));

//        long offset0 = consumer.position(new TopicPartition("tp_demo_01", 0));
//        System.out.println("当前主题在0号分区上的位移："+offset0);

        long offset0 = consumer.position(new TopicPartition("tp_demo_01", 0));
        long offset1 = consumer.position(new TopicPartition("tp_demo_01", 1));
        long offset2 = consumer.position(new TopicPartition("tp_demo_01", 2));

        System.out.println(offset0);
        System.out.println(offset1);
        System.out.println(offset2);

        consumer.seekToBeginning(Arrays.asList(
                new TopicPartition("tp_demo_01",0)
        ));
        consumer.seek(new TopicPartition("tp_demo_01", 1), 10);

        offset0 = consumer.position(new TopicPartition("tp_demo_01", 0));
        offset1 = consumer.position(new TopicPartition("tp_demo_01", 1));
        offset2 = consumer.position(new TopicPartition("tp_demo_01", 2));

        System.out.println(offset0);
        System.out.println(offset1);
        System.out.println(offset2);


//        //获取给当前消费组分配的主题分区信息
//        Set<TopicPartition> assignment = consumer.assignment();
//
//        for (TopicPartition partition : assignment) {
//            System.out.println(partition);
//        }

        consumer.close();
    }
}
