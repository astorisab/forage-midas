package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public UserRecord save(UserRecord userRecord) {
        return userRepository.save(userRecord);
    }
   
    
    public TransactionRecord save(TransactionRecord transactionRecord) {
    	
    	return transactionRepository.save(transactionRecord);
    }
    
    
    
    public UserRecord update(UserRecord user) {
    	return userRepository.saveAndFlush(user);
    }
    
    public Optional<UserRecord> findById(long id) {
    	return Optional.ofNullable(userRepository.findById(id));
    }
    
    
    
    public boolean validateTransaction(Transaction transaction) {
    	Optional<UserRecord> senderId = findById(transaction.getSenderId());
    	Optional<UserRecord> recipientId = findById(transaction.getRecipientId());
    	if(senderId.isEmpty() || recipientId.isEmpty() || transaction.getAmount() >= senderId.get().getBalance()) {
    		return false;
    	}
    	return true;
    }

}
