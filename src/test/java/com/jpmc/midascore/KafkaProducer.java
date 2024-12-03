package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
	
    private final String topic;
    
    static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(@Value("${general.kafka-topic}") String topic) {
        this.topic = topic;
    }

    public void send(String transactionLine) {	
        String[] transactionData = transactionLine.split(", ");  
        kafkaTemplate.send(topic, new Transaction(Long.parseLong(transactionData[0]), Long.parseLong(transactionData[1]), new BigDecimal(transactionData[2])));
    }
    
}