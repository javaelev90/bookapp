package se.library.bookapp.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = LibraryController.class)
public class LibraryControllerExceptionHandler{
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleError() {
		System.out.println("hehehe");
		return new ResponseEntity<>("Something went wrong probably because of bad input",HttpStatus.NOT_FOUND);
	}
	
}
