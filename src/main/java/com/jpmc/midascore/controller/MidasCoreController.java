package com.jpmc.midascore.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;

@RestController
@CrossOrigin(origins = "http://localhost:8081")
public class MidasCoreController {

	
	@Autowired
	DatabaseConduit databaseConduit;
	
//	@GetMapping("/balance")
//	public ResponseEntity<String> balanceWelcomeScreen(){
//		return ResponseEntity.ok("<h1>Welcome to the Balance API</h1>");
//	}
	
	
	@GetMapping("/balance")
	public Balance getBalance(@RequestParam("userId") Long userId){
		
		Optional<UserRecord> currentUser = databaseConduit.findById(userId);
		System.out.println(String.valueOf(userId));
		if(currentUser.isEmpty()) return new Balance(new BigDecimal(0));
		return new Balance(currentUser.get().getBalance());
	}
	
}
