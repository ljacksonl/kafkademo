package com.lagou.kafka.produce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MyProducer3 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Map<String,Object> configs = new HashMap<String, Object>();
        // 设置连接Kafka的初始连接用到的服务器地址
        // 指定初始连接用到的broker地址
        configs.put("bootstrap.servers","192.168.80.7:9092");
        // 指定key的序列化类
        configs.put("key.serializer", IntegerSerializer.class);
        // 指定value的序列化类
        configs.put("value.serializer", StringSerializer.class);

//        configs.put("acks","all");
//        configs.put("reties","3");

        KafkaProducer<Integer,String> producer = new KafkaProducer<Integer, String>(configs);

        //用于设置用户自定义的消息头字段
        List<Header> headers = new ArrayList<Header>();
        headers.add(new RecordHeader("biz.name","producer.demo".getBytes()));

        for (int i = 0; i < 100; i++) {
            ProducerRecord<Integer,String> record = new ProducerRecord<Integer, String>(
                    "topic_1",
                    0,
                    i,
                    "hello lagou " + i,
                    headers

            );
            //消息的异步确认
            producer.send(record, new Callback() {
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null){
                        System.out.println("消息的主题："+ recordMetadata.topic());
                        System.out.println("消息的分区号："+ recordMetadata.partition());
                        System.out.println("消息的偏移量："+ recordMetadata.offset());
                    }else {
                        System.out.println("异常消息："+ e.getMessage());
                    }
                }
            });
        }




        //关闭生产者
        producer.close();
    }
}
