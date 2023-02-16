package com.lagou.kafka.demo.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class MyPartitioner implements Partitioner {
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        // 此处可以计算分区的数字
        // 我们直接指定为2

        return 2;
    }

    public void close() {

    }

    public void configure(Map<String, ?> map) {

    }
}
