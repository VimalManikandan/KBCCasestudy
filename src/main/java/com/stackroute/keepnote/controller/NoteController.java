package com.stackroute.keepnote.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NotAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.Responce;
import com.stackroute.keepnote.service.NoteService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */

@RestController
@RequestMapping("/note")
public class NoteController {

	/*
	 * Autowiring should be implemented for the NoteService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	@Autowired
	private NoteService noteService;

	public NoteController(NoteService noteService) {
		this.noteService=noteService;
	}

	/*
	 * Define a handler method which will create a specific note by reading the
	 * Serialized object from request body and save the note details in a Note table
	 * in the database.Handle ReminderNotFoundException and
	 * CategoryNotFoundException as well. please note that the loggedIn userID
	 * should be taken as the createdBy for the note.This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 201(CREATED) - If the note created successfully. 2. 409(CONFLICT) - If the
	 * noteId conflicts with any existing user 3. 401(UNAUTHORIZED) - If the user
	 * trying to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/note" using HTTP POST method
	 */
	
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Responce> createNote(@RequestBody Note note,HttpServletRequest request) throws Exception {
		
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, note.getCreatedBy()) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("Un Authorized");
		}
			
		Responce responce = new Responce();
		try {
			if(noteService.createNote(note)) {
				responce.setMessage("CREATED");
				responce.setStatus(HttpStatus.CREATED.value());
			}
			else {
				responce.setMessage("FAILED");
				responce.setStatus(HttpStatus.BAD_REQUEST.value());				
			}

		}
		catch(CategoryNotFoundException e) {
			throw e;
		}
		catch(ReminderNotFoundException e ) {
			throw e;
		}
		catch(NotAlreadyExistsException e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce, HttpStatus.OK);
	}
	
	
	

	/*
	 * Define a handler method which will delete a note from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the note with specified noteId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid noteId without {}
	 */

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<Responce> deleteCategory(@PathVariable int id, HttpServletRequest request)
			throws UserUnAuthorized, NoteNotFoundException {
		Responce responce = new Responce();
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		try {
			int rslt = (usersList != null)
					? Collections.frequency(usersList, noteService.getNoteById(id).getCreatedBy())
					: 0;
			if (rslt == 0) {
				throw new UserUnAuthorized("UnAuthorized");
			}
			if(noteService.deleteNote(id)) {
				responce.setMessage("OK");
				responce.setStatus(HttpStatus.OK.value());
			}
			else {				
					responce.setMessage("FAILED");
					responce.setStatus(HttpStatus.BAD_REQUEST.value());
			}

		} catch (NoteNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce, HttpStatus.OK);

	}
	

	/*
	 * Define a handler method which will update a specific note by reading the
	 * Serialized object from request body and save the updated note details in a
	 * note table in database handle ReminderNotFoundException,
	 * NoteNotFoundException, CategoryNotFoundException as well. please note that
	 * the loggedIn userID should be taken as the createdBy for the note. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note updated successfully. 2.
	 * 404(NOT FOUND) - If the note with specified noteId is not found. 3.
	 * 401(UNAUTHORIZED) - If the user trying to perform the action has not logged
	 * in.
	 * 
	 * This handler method should map to the URL "/note/{id}" using HTTP PUT method.
	 */
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Responce> updateNote(@RequestBody Note note, @PathVariable int id,
			HttpServletRequest request) throws Exception {

		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList,noteService.getNoteById(id).getCreatedBy() ) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}
		
		
		Responce responce = new Responce();
		try {
			if (noteService.updateNote(note, id)) {
				responce.setMessage("OK");
				responce.setStatus(HttpStatus.OK.value());
				return new ResponseEntity<Responce>(responce, HttpStatus.OK);
			} else {
				responce.setMessage("OK");
				responce.setStatus(HttpStatus.OK.value());
				return new ResponseEntity<Responce>(responce, HttpStatus.BAD_REQUEST);
			}
		} catch (NoteNotFoundException e) {
			throw e;
		}
		
	}
	
	
	/*
	 * Define a handler method which will get us the notes by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the note found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/note" using HTTP GET method
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Note>> getAllNoteByUserId(@RequestParam String id, HttpSession session)
			throws UserUnAuthorized, UserNotFoundException {
		Responce responce = new Responce();
		List<String> usersList = (List<String>) session.getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, id) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("Un Authorized");
		}
		List<Note> notes=null;
		notes= noteService.getAllNotesByUserId(id);
		responce.setMessage("OK");
		responce.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
	}
	

}
