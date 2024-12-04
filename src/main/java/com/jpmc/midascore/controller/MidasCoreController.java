package com.jpmc.midascore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MidasCoreController {

	@GetMapping("/balance")
	public ResponseEntity<String> balanceWelcomeScreen(){
		return ResponseEntity.ok("<h1>Welcome to the Balance API</h1>");
	}
	
	
	@GetMapping("/balance/{userId}")
	public ResponseEntity<String> getBalance(@PathVariable long userId){
		
		
		return ResponseEntity.ok(String.valueOf(userId));
	}
	
}
