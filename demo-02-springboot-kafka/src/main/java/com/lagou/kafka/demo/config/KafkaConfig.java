package com.lagou.kafka.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.ByteBufferSerializer;
import org.apache.kafka.common.serialization.BytesSerializer;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic1(){
        return new NewTopic("nptc-01",3,(short)1);

    }

    @Bean
    public NewTopic topic2(){
        return new NewTopic("nptc-02",5,(short)1);
    }


    public KafkaAdmin kafkaAdmin(){
        Map<String,Object> config = new HashMap<>();
        config.put("bootstrap.servers","linux123:9092");
        KafkaAdmin admin = new KafkaAdmin(config);
        return admin;
    }

//    @Bean
//    @Autowired
//    public KafkaTemplate<Integer,String> kafkaTemplate(ProducerFactory<Integer,String> producerFactory){
//
//
//    }


}
