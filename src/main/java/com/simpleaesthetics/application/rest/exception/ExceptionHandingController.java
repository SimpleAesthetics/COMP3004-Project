package com.simpleaesthetics.application.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.simpleaesthetics.application.rest.db.DatabaseException;

@Controller
public class ExceptionHandingController {
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<Object> handleDatabaseException(DatabaseException ex) {
		return buildResponseEntity(ex);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleDatabaseException(Exception ex) {
		return buildResponseEntity(ex);
	}
	
	private ResponseEntity<Object> buildResponseEntity(Exception ex) {
	       return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	   }
}
