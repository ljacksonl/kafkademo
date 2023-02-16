package com.lagou.kafka.demo;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.errors.LeaderNotAvailableException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.channels.Pipe;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MyAdminClient {

    private KafkaAdminClient client;

    @Before
    public void before(){
        Map<String,Object> conf = new HashMap<String, Object>();
        conf.put("bootstrap.servers", "linux123:9092");
        conf.put("client.id", "adminclient-1");
        client = (KafkaAdminClient) KafkaAdminClient.create(conf);
    }

    @After
    public void after(){
        client.close();
    }

    @Test
    public void testListTopics1() throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = client.listTopics();
        KafkaFuture<Collection<TopicListing>> listings = listTopicsResult.listings();
        Collection<TopicListing> topicListings = listings.get();

        topicListings.forEach(new Consumer<TopicListing>() {
            @Override
            public void accept(TopicListing topicListing) {
                boolean internal = topicListing.isInternal();
                String name = topicListing.name();
                String s = topicListing.toString();
                System.out.println(s + "\t" + name + "\t" + internal);
            }
        });

        KafkaFuture<Set<String>> names = listTopicsResult.names();
        Set<String> strings = names.get();
        strings.forEach(name -> {
            System.out.println(name);
        });

        KafkaFuture<Map<String, TopicListing>> mapKafkaFuture = listTopicsResult.namesToListings();
        Map<String, TopicListing> stringTopicListingMap = mapKafkaFuture.get();
        stringTopicListingMap.forEach((k,v) ->{
            System.out.println(k + v);
        });

    }

    @Test
    public void testListTopics2() throws ExecutionException, InterruptedException {
        ListTopicsOptions options = new ListTopicsOptions();
        options.listInternal(false);
        options.timeoutMs(500);
        ListTopicsResult listTopicsResult = client.listTopics(options);
        Map<String, TopicListing> stringTopicListingMap = listTopicsResult.namesToListings().get();
        stringTopicListingMap.forEach(new BiConsumer<String, TopicListing>() {
            @Override
            public void accept(String s, TopicListing topicListing) {
                System.out.println(s + topicListing);
            }
        });
    }

    @Test
    public void testCreateTopic() throws ExecutionException, InterruptedException {
        Map<String,String> configs = new HashMap<>();
        configs.put("max.message.bytes", "1048576");
        configs.put("segment.bytes", "1048576000");

        NewTopic newTopic = new NewTopic("adm_tp_01", 2, (short) 1);
        newTopic.configs(configs);

        CreateTopicsResult topics = client.createTopics(Collections.singleton(newTopic));
        KafkaFuture<Void> all = topics.all();
        Void aVoid = all.get();
        System.out.println(aVoid);


    }

    @Test
    public void testDeleteTopic() throws ExecutionException, InterruptedException {
        DeleteTopicsOptions options = new DeleteTopicsOptions();
        options.timeoutMs(5000);
        DeleteTopicsResult deleteResult = client.deleteTopics(Collections.singleton("topic_1"), options);
        deleteResult.all().get();
    }

    @Test
    public void testAlterTopic() throws ExecutionException, InterruptedException {
        NewPartitions newPartitions = NewPartitions.increaseTo(5);
        Map<String, NewPartitions> newPartitionsMap = new HashMap<>();
        newPartitionsMap.put("adm_tp_01", newPartitions);
        CreatePartitionsOptions option = new CreatePartitionsOptions();
// Set to true if the request should be validated without creating new partitions.
// 如果只是验证，而不创建分区，则设置为true
// option.validateOnly(true);
                CreatePartitionsResult partitionsResult = client.createPartitions(newPartitionsMap, option);
        Void aVoid = partitionsResult.all().get();
    }

    @Test
    public void testDescribeTopics() throws ExecutionException, InterruptedException {
        DescribeTopicsOptions options = new DescribeTopicsOptions();
        options.timeoutMs(3000);
        DescribeTopicsResult topicsResult = client.describeTopics(Collections.singleton("adm_tp_01"), options);
        Map<String, TopicDescription> stringTopicDescriptionMap = topicsResult.all().get();
        stringTopicDescriptionMap.forEach((k, v) -> {
            System.out.println(k + "\t" + v);
            System.out.println("=======================================");
            System.out.println(k);
            boolean internal = v.isInternal();
            String name = v.name();
            List<TopicPartitionInfo> partitions = v.partitions();
            String partitionStr = Arrays.toString(partitions.toArray());
            System.out.println("内部的？" + internal);
            System.out.println("topic name = " + name);
            System.out.println("分区：" + partitionStr);
            partitions.forEach(partition -> {
                System.out.println(partition);
            });
        });
    }

    @Test
    public void testDescribeCluster() throws ExecutionException, InterruptedException {
        DescribeClusterResult describeClusterResult = client.describeCluster();
        KafkaFuture<String> stringKafkaFuture = describeClusterResult.clusterId();
        String s = stringKafkaFuture.get();
        System.out.println("cluster name = " + s);
        KafkaFuture<Node> controller = describeClusterResult.controller();
        Node node = controller.get();
        System.out.println("集群控制器：" + node);
        Collection<Node> nodes = describeClusterResult.nodes().get();
        nodes.forEach(node1 -> {
            System.out.println(node1);
        });
    }

    @Test
    public void testDescribeConfigs() throws ExecutionException, InterruptedException, TimeoutException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.BROKER, "0");
        DescribeConfigsResult describeConfigsResult=client.describeConfigs(Collections.singleton(configResource));

        Map<ConfigResource, Config> configMap = describeConfigsResult.all().get(15, TimeUnit.SECONDS);
        configMap.forEach(new BiConsumer<ConfigResource, Config>() {
            @Override
            public void accept(ConfigResource configResource, Config
                    config) {
                ConfigResource.Type type = configResource.type();
                String name = configResource.name();
                System.out.println("资源名称：" + name);
                Collection<ConfigEntry> entries = config.entries();
                entries.forEach(new Consumer<ConfigEntry>() {
                    @Override
                    public void accept(ConfigEntry configEntry) {
                        boolean aDefault = configEntry.isDefault();
                        boolean readOnly = configEntry.isReadOnly();
                        boolean sensitive = configEntry.isSensitive();
                        String name1 = configEntry.name();
                        String value = configEntry.value();
                        System.out.println("是否默认：" + aDefault + "\t是否只读？" + readOnly + "\t是否敏感？" + sensitive
                                        + "\t" + name1 + " --> " + value);
                    }
                });
                ConfigEntry retries = config.get("retries");
                if (retries != null) {
                    System.out.println(retries.name() + " -->" +
                            retries.value());
                } else {
                    System.out.println("没有这个属性");
                }
            }
        });
    }
}
