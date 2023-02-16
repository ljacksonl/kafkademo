package com.lagou.kafka.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import javax.sound.midi.Soundbank;
import java.util.HashMap;
import java.util.Map;

public class MyProducer {

    public static void main(String[] args) {

        Map<String,Object> configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"linux123:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);

        configs.put("classContent","this is lagou's kafka class");

        // interceptor.classes
        //如果有多个拦截器，则设置为多个拦截器类的全限定类名，中间用逗号隔开

        configs.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,"com.lagou.kafka.demo.interceptor.IntercetorOne,"+
                "com.lagou.kafka.demo.interceptor.IntercetorTwo," +
                "com.lagou.kafka.demo.interceptor.IntercetorThree");

        KafkaProducer<Integer,String> producer = new KafkaProducer<Integer, String>(configs);

        ProducerRecord<Integer,String> record = new ProducerRecord<Integer, String>(
                "tp_inter_01",
                0,
                1001,
                "this is lagou's 1001 message"
        );

        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null){
                    System.out.println(metadata.offset());
                }
            }
        });

        //关闭生产者
        producer.close();
    }
}
