package co.id.bcafinance.hlbooking.broker;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:38
@Last Modified 05/05/2024 20:38
Version 1.0
*/

import co.id.bcafinance.hlbooking.configuration.KafkaProConfig;
import co.id.bcafinance.hlbooking.configuration.LocalDateAdapter;
import co.id.bcafinance.hlbooking.configuration.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Properties;

/**
 * Hit Producer for kafka
 **/
public class HitProducer {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .create();

    public void producerHitTopics(List lt, String topics) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProConfig.getKafkaProHost());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        for (int i = 0; i < lt.size(); i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topics, gson.toJson(lt.get(i)));
            producer.send(record);
            System.out.println("Data ke " + i + " : " + lt.get(i));
        }
        producer.close();
    }
}

