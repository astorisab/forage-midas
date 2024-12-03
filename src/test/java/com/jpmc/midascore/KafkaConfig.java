package com.jpmc.midascore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class KafkaConfig {

    @Bean
    public BytesJsonMessageConverter bytesJsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Customize objectMapper if needed
        return new BytesJsonMessageConverter(objectMapper); 
    }
}