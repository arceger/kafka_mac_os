package com.logs.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaMessageConsumer {
    private static final Log log = LogFactory.getLog(KafkaMessageConsumer.class);

    public static void consumeMessages() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("topic1"));

        long pollTimeoutMillis = 100; // Tempo de espera para poll
        long noMessagesTimeoutMillis = 5000; // Tempo máximo de espera sem novas mensagens
        long lastMessageTime = System.currentTimeMillis();

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(pollTimeoutMillis));
            if (records.isEmpty()) {
                if (System.currentTimeMillis() - lastMessageTime > noMessagesTimeoutMillis) {
                    log.info("Nenhuma nova mensagem recebida nos últimos " + noMessagesTimeoutMillis + "ms. Encerrando consumidor.");
                    break;
                }
            } else {
                lastMessageTime = System.currentTimeMillis();
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Recebendo mensagem: " + record.value() + ", Partição: " + record.partition() + ", Offset: " + record.offset());
                }
            }
        }

        consumer.close();
        log.info("Consumer fechado.");
    }
}