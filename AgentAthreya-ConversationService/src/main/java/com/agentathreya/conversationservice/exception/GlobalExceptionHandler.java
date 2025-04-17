package com.agentathreya.conversationservice.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllExceptions(Exception ex) {
		Map<String, Object> errorDetails = new LinkedHashMap<>();
		errorDetails.put("timestamp", LocalDateTime.now());
		errorDetails.put("message", ex.getMessage());
		errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

		// Log the error (later we can connect this to external monitoring systems)
		logger.error("Exception caught: ", ex);

		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
