package com.simpleaesthetics.application.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.simpleaesthetics.application.rest.db.DatabaseException;

@Controller
public class ExceptionHandingController {
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<String> handleDatabaseException(DatabaseException ex) {
		return buildResponseEntity(ex, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleDatabaseException(Exception ex) {
		return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private ResponseEntity<String> buildResponseEntity(Exception ex, HttpStatus status) {
	       return new ResponseEntity<String>(ex.getMessage(), status);
	   }
}
