package com.jpmc.midascore;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.SerializationUtils;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.com.google.common.base.Optional;

import java.io.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.*;
import com.jpmc.midascore.foundation.Transaction;

@Component
public class KafkaConsumer {

		static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
		private Transaction transactionData;
		@Autowired
		private DatabaseConduit databaseConduit; 
		
		
		@KafkaListener(topics="${general.kafka-topic}", groupId="midas-group")
		public void listen(Object transaction) {
			byte[] data =  (byte[]) ((ConsumerRecord) transaction).value();
			String value = new String(data);
			ObjectMapper mapper = new ObjectMapper();
			try {
				transactionData = mapper.readValue(value, Transaction.class);
				transactionStore();
//				String[] transactionPartition = String.valueOf(transactionData.getAmount()).split("[.]");
//				java.util.Optional<UserRecord> sender = databaseConduit.findById(transactionData.getSenderId());
//				String[] senderBalancePartition = String.valueOf(sender.get().getBalance()).split("[.]");
//				getIntegerDecimalSubtraction(transactionPartition, senderBalancePartition);
//				java.util.Optional<UserRecord> recipient = databaseConduit.findById(transactionData.getRecipientId());
			
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    
		}
		
		private boolean transactionStore() {
			TransactionRecord tData = null;
			if(databaseConduit.validateTransaction(transactionData)) {
				tData = databaseConduit.save(new TransactionRecord(transactionData));
//				logger.info(String.format("Amount attached to transaction %o: %.02f", tData.getId(), transactionData.getAmount()));

				return true;
			}
			logger.info("Not valid:" + transactionData.toString());
			return true;
		}
		
		
		private int getIntegerDecimalSubtraction(String[] transactionPartition, String[] senderPartition) {
			int currentTransactionCents = Integer.valueOf(transactionPartition[1]);
			int currentSenderAmountCents = Integer.valueOf(senderPartition[1]);
			int centDifference = currentSenderAmountCents - currentTransactionCents;
			if(centDifference < 0) {
				return (Integer.parseInt(transactionPartition[0]) * 100) + ((1 + centDifference));
			}
			return (Integer.parseInt(transactionPartition[0]) * 100) + centDifference;
		}
		
//		private int getIntegerDecimalAddition(String[] transactionPartition, String[] senderPartition) {
//			int currentTransactionCents = Integer.valueOf(transactionPartition[1]);
//			int currentSenderAmountCents = Integer.valueOf(senderPartition[1]);
//			int centDifference = currentSenderAmountCents - currentTransactionCents;
//			if(centDifference < 0) {
//				return (Integer.parseInt(transactionPartition[0]) * 100) + ((1 + centDifference));
//			}
//			return (Integer.parseInt(transactionPartition[0]) * 100) + centDifference;
//		}


}
