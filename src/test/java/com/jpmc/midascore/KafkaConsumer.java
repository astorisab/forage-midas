package com.jpmc.midascore;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.SerializationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.com.google.common.base.Optional;

import java.io.*;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.*;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {

		static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
		private Transaction transactionData;
		java.util.Optional<UserRecord> sender;
		java.util.Optional<UserRecord> recipient;
		@Autowired
		private DatabaseConduit databaseConduit; 
		@Autowired
		RestTemplate restTemplate;
		
		
		
		@KafkaListener(topics="${general.kafka-topic}", groupId="midas-group")
		public void listen(Object transaction) {
			byte[] data =  (byte[]) ((ConsumerRecord) transaction).value();
			String value = new String(data);
			ObjectMapper mapper = new ObjectMapper();
			
			try {
				transactionData = mapper.readValue(value, Transaction.class);

					
			if(databaseConduit.validateTransaction(transactionData)) {	
					
				transactionStore();
			    sender = databaseConduit.findById(transactionData.getSenderId());			    
			    recipient = databaseConduit.findById(transactionData.getRecipientId());
			    BigDecimal incentive = getIncentiveValue(transactionData);
			    senderBalanceSubtraction();   
			    recipientBalanceAddition(incentive);
			    
			    
			    
//			    logger.info(String.format("Recipient incentive: %s", incentive.toString()));
//                System.out.println(databaseConduit.findById(sender.get().getId()).get().getBalance());
//                System.out.println(databaseConduit.findById(recipient.get().getId()).get().getBalance());
//			    
//                System.out.print("Wilbur's balance is: ");
//                System.out.println(databaseConduit.findById(9).get().getBalance());
                
                
                
                
			} else {
//				logger.info("Not valid:" + transactionData.toString());
			}   
			    
			    
			  
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
		}
		
		private void transactionStore() {
			TransactionRecord tData = null;
			
			tData = databaseConduit.save(new TransactionRecord(transactionData));
//			logger.info(String.format("Amount attached to transaction %o: %f", tData.getId(), transactionData.getAmount()));

			

		}
		
		private void senderBalanceSubtraction() {
			BigDecimal senderAmount = sender.get().getBalance();
			BigDecimal senderSubtraction = transactionData.getAmount();
			UserRecord updatedSender = sender.get();
			updatedSender.setBalance(senderAmount.subtract(senderSubtraction));
			databaseConduit.update(updatedSender);
			
		}
		private void recipientBalanceAddition(BigDecimal incentive) {
			BigDecimal recipientAmount = recipient.get().getBalance();
			BigDecimal recipientAddition = transactionData.getAmount().add(incentive);
			UserRecord updatedRecipient = recipient.get();
			updatedRecipient.setBalance(recipientAmount.add(recipientAddition));
			databaseConduit.update(updatedRecipient);
	
		}
		
		private BigDecimal getIncentiveValue(Transaction transaction) {
			ResponseEntity<Incentive> value = restTemplate.postForEntity("http://localhost:8080/incentive", transaction, Incentive.class);			
			return value.getBody().getAmount();
		}
		
		
		



}
