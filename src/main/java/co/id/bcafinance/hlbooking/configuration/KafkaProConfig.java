package co.id.bcafinance.hlbooking.configuration;
/*
IntelliJ IDEA 2022.3.1 (Community Edition)
Build #IC-223.8214.52, built on December 20, 2022
@Author Heinrich a.k.a. Heinrich Lizato
Java Developer
Created on 05/05/2024 20:34
@Last Modified 05/05/2024 20:34
Version 1.0
*/

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:kafkaconfig.properties")
public class KafkaProConfig {
    private static String kafkaProHost;
    private static String kafkaProTopics;

    public static String getKafkaProHost() {
        return kafkaProHost;
    }

    @Value("${kafka.pro.host}")
    private void setKafkaProHost(String kafkaProHost) {
        KafkaProConfig.kafkaProHost = kafkaProHost;
    }

    public static String getKafkaProTopics() {
        return kafkaProTopics;
    }

    @Value("${kafka.pro.topics}")
    private void setKafkaProTopics(String kafkaProTopics) {
        KafkaProConfig.kafkaProTopics = kafkaProTopics;
    }
}

