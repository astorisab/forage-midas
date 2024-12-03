package com.jpmc.midascore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {

		static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

		@KafkaListener(topics="${general.kafka-topic}", groupId="midas-group")
		public void listen(Object transaction) {
			logger.info("hello");
//			logger.info(String.format("User created -> %s", transaction));			
//			logger.info(String.valueOf(((Transaction) transaction).getAmount()));
		}
		


}
