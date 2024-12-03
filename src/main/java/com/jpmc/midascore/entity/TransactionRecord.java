package com.jpmc.midascore.entity;

import java.math.BigDecimal;

import com.jpmc.midascore.foundation.Transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;


@Entity
public class TransactionRecord {

	@Id
    @GeneratedValue()
    private long id;
	
	@Column(nullable = false)
	private long senderId;
	
	@Column(nullable = false)
	private long recipientId;
	
	@Column(nullable = false)
	private BigDecimal amount;

	
	public TransactionRecord(Transaction transaction) {
		this.senderId = transaction.getSenderId();
		this.recipientId = transaction.getRecipientId();
		this.amount = transaction.getAmount();
	}
	
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(long recipientId) {
		this.recipientId = recipientId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
