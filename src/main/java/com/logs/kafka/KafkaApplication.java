package com.logs.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication {

	private static final Log log = LogFactory.getLog(KafkaApplication.class);

	public static void main(String[] args) {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		KafkaProducer<String, String> producer = new KafkaProducer<>(props);

		for (int i = 1; i <= 10; i++) {
			String message = "Mensagem test" + i;
			ProducerRecord<String, String> record = new ProducerRecord<>("topic1", message);
			try {
				// Envia a mensagem e espera pela confirmação
				RecordMetadata metadata = producer.send(record).get();
				log.info("Mensagem enviada com sucesso: " + message + " para o tópico " + metadata.topic() + " na partição " + metadata.partition() + " com offset " + metadata.offset());
			} catch (ExecutionException | InterruptedException e) {
				//e.printStackTrace();
				log.error("erro ao postar mensagem");
			}
		}

		producer.close();

		try {
			Thread.sleep(3000);
			log.info(".....Aguardando para ler as postagens");
		} catch (InterruptedException e) {
			log.error("Erro durante a pausa de 3 segundos", e);
		}

		// Chama o consumidor para ler as mensagens
		KafkaMessageConsumer.consumeMessages();
	}

}


