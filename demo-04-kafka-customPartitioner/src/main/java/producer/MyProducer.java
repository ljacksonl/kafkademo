package producer;

import com.lagou.kafka.demo.partitioner.MyPartitioner;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import sun.awt.image.OffScreenImage;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;

public class MyProducer {
    public static void main(String[] args) {

        Map<String,Object> configs = new HashMap<String, Object>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"linux123:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);

        //指定自定义的分区器
        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class);



        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(configs);

        //此处不要设置partition的值
        ProducerRecord<String,String> record = new ProducerRecord<String, String>(
            "tp_part_01",
                "mykey",
                "myvalue"
        );

        producer.send(record, new Callback() {
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if (e != null){
                    System.out.println("消息发送失败");
                }else {
                    System.out.println(recordMetadata.topic());
                    System.out.println(recordMetadata.partition());
                    System.out.println(recordMetadata.offset());
                }
            }
        });

        //关闭生产者
        producer.close();

    }
}
