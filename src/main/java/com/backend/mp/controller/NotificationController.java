package com.backend.mp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.mp.dto.NotificationWebHooksDTO;
import com.backend.mp.util.WrapperResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = { "*" }) 
@RestController 
@RequestMapping("/notification")
public class NotificationController {

	@PostMapping("/create")
	public ResponseEntity<?> createWebHooks(@RequestBody NotificationWebHooksDTO request) {
		log.info("Json Webhooks");
		log.info(request.toString());
	
		return new WrapperResponse<>(true, "Notification created.", null).createResponse(HttpStatus.CREATED);
	}
}
