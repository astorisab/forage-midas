package com.jpmc.midascore;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.SerializationUtils;
import org.springframework.stereotype.Component;
import java.io.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {

		static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

		@KafkaListener(topics="${general.kafka-topic}", groupId="midas-group")
		public void listen(Object transaction) {
			byte[] data =  (byte[]) ((ConsumerRecord) transaction).value();
			String value = new String(data);
			ObjectMapper mapper = new ObjectMapper();
			try {
				Transaction transactionData = mapper.readValue(value, Transaction.class);
				logger.info(value);
				logger.info(Float.toString(transactionData.getAmount()));
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		    
		    
		}
		


}
