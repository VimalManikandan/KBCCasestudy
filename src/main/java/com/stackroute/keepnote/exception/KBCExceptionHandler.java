package com.stackroute.keepnote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.stackroute.keepnote.model.Responce;

@ControllerAdvice
public class KBCExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Responce> handleException(Exception e) {

		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<Responce> handleException(UserNotFoundException e) {

		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(value = UserAlreadyExistException.class)
	public ResponseEntity<Responce> handleException(UserAlreadyExistException e) {

		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.CONFLICT.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(value = UserUnAuthorized.class)
	public ResponseEntity<Responce> handleException(UserUnAuthorized e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.UNAUTHORIZED.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.UNAUTHORIZED);

	}
	
	@ExceptionHandler(value = ReminderAlreadyExistException.class)
	public ResponseEntity<Responce> handleException(ReminderAlreadyExistException e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.CONFLICT.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(value = ReminderNotFoundException.class)
	public ResponseEntity<Responce> handleException(ReminderNotFoundException e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(value = CategoryAlreadyExistException.class)
	public ResponseEntity<Responce> handleException(CategoryAlreadyExistException e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.CONFLICT.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(value = CategoryNotFoundException.class)
	public ResponseEntity<Responce> handleException(CategoryNotFoundException e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.NOT_FOUND);

	}
	
	
	@ExceptionHandler(value = NotAlreadyExistsException.class)
	public ResponseEntity<Responce> handleException(NotAlreadyExistsException e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.CONFLICT.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(value = NoteNotFoundException.class)
	public ResponseEntity<Responce> handleException(NoteNotFoundException e) {
		Responce responce = new Responce();
		responce.setMessage(e.getMessage());
		responce.setStatus(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<Responce>(responce, HttpStatus.NOT_FOUND);

	}
	
	
	

}
