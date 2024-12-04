package com.jpmc.midascore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MidasCoreController {

	@GetMapping("/balance")
	public ResponseEntity<String> getBalance(){
		
		return ResponseEntity.ok("hello");
	}
	
}
