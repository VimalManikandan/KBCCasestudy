package com.stackroute.keepnote.controller;

import java.util.Collections;
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

import com.stackroute.keepnote.exception.ReminderAlreadyExistException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.Responce;
import com.stackroute.keepnote.service.ReminderService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */

@RestController
@RequestMapping("/reminder")
public class ReminderController {

	/*
	 * From the problem statement, we can understand that the application requires
	 * us to implement five functionalities regarding reminder. They are as
	 * following:
	 * 
	 * 1. Create a reminder 2. Delete a reminder 3. Update a reminder 2. Get all
	 * reminders by userId 3. Get a specific reminder by id.
	 * 
	 * we must also ensure that only a user who is logged in should be able to
	 * perform the functionalities mentioned above.
	 * 
	 */

	/*
	 * Autowiring should be implemented for the ReminderService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
	
	private ReminderService reminderService;

	@Autowired
	public ReminderController(ReminderService reminderService) {
		this.reminderService = reminderService;
	}


	/*
	 * Define a handler method which will create a reminder by reading the
	 * Serialized reminder object from request body and save the reminder in
	 * reminder table in database. Please note that the reminderId has to be unique
	 * and the loggedIn userID should be taken as the reminderCreatedBy for the
	 * reminder. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED - In case of successful
	 * creation of the reminder 2. 409(CONFLICT) - In case of duplicate reminder ID
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/reminder" using HTTP POST
	 * method".
	 */
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Reminder> createReminder(@RequestBody Reminder reminder,HttpServletRequest request) throws Exception {
		
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, reminder.getReminderCreatedBy()) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("Un Authorized");
		}
		
		Reminder reminder2=null;
		try {
			reminder2=reminderService.createReminder(reminder);
			if(reminder2!=null) {
				return new ResponseEntity<Reminder>(reminder2, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<Reminder>(reminder2, HttpStatus.BAD_REQUEST); 			
			}

		}
		catch(ReminderAlreadyExistException e) {
			throw e;
		}
		
	}
	
	

	/*
	 * Define a handler method which will delete a reminder from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the reminder deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the reminder with specified reminderId is
	 * not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/reminder/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid reminderId without {}
	 */
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<Responce> deleteReminder(@PathVariable int id, HttpServletRequest request)
			throws UserUnAuthorized, ReminderNotFoundException {
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		
		int rslt = (usersList != null) ? Collections.frequency(usersList, reminderService.getReminderById(id).getReminderCreatedBy()) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}

		Responce responce = new Responce();
		try {
			reminderService.deleteReminder(id);
			responce.setMessage("OK");
			responce.setStatus(HttpStatus.OK.value());

		} catch (ReminderNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce, HttpStatus.OK);

	}
	

	/*
	 * Define a handler method which will update a specific reminder by reading the
	 * Serialized object from request body and save the updated reminder details in
	 * a reminder table in database handle ReminderNotFoundException as well. please
	 * note that the loggedIn userID should be taken as the reminderCreatedBy for
	 * the reminder. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the reminder updated
	 * successfully. 2. 404(NOT FOUND) - If the reminder with specified reminderId
	 * is not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/reminder/{id}" using HTTP PUT
	 * method.
	 */
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Reminder> updateReminder(@RequestBody Reminder reminder, @PathVariable int id,
			HttpServletRequest request) throws Exception {

		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList,reminderService.getReminderById(id).getReminderCreatedBy() ) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}

		Reminder reminderr=null;
		try {
			reminderr=reminderService.updateReminder(reminder, id);
			if (reminderr != null) {
				return new ResponseEntity<Reminder>(reminderr, HttpStatus.OK);
			} else {
				return new ResponseEntity<Reminder>(reminderr, HttpStatus.BAD_REQUEST);
			}
		} catch (ReminderNotFoundException e) {
			throw e;
		}
		
	}

	
	
	/*
	 * Define a handler method which will get us the reminders by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the reminder found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/reminder" using HTTP GET method
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Reminder>> getReminderByUserId(@RequestParam String id, HttpSession session)
			throws UserUnAuthorized, UserNotFoundException {
		Responce responce = new Responce();
		List<String> usersList = (List<String>) session.getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, id) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("Un Authorized");
		}
		List<Reminder> reminders=null;
		try {
			reminders= reminderService.getAllReminderByUserId(id);
			responce.setMessage("OK");
			responce.setStatus(HttpStatus.OK.value());
		} catch (UserNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<List<Reminder>>(reminders, HttpStatus.OK);
	}
	
	
	

	/*
	 * Define a handler method which will show details of a specific reminder handle
	 * ReminderNotFoundException as well. This handler method should return any one
	 * of the status messages basis on different situations: 1. 200(OK) - If the
	 * reminder found successfully. 2. 401(UNAUTHORIZED) - If the user trying to
	 * perform the action has not logged in. 3. 404(NOT FOUND) - If the reminder
	 * with specified reminderId is not found. This handler method should map to the
	 * URL "/reminder/{id}" using HTTP GET method where "id" should be replaced by a
	 * valid reminderId without {}
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<Reminder> getReminder(@PathVariable int id, HttpSession session)
			throws ReminderNotFoundException, UserUnAuthorized {
		Responce responce = new Responce();
		Reminder reminder=null;
		try {
			reminder = reminderService.getReminderById(id);

			List<String> usersList = (List<String>) session.getAttribute("Logged_users");
			int rslt = (usersList != null) ? Collections.frequency(usersList, reminder.getReminderCreatedBy()) : 0;
			if (rslt == 0) {
				throw new UserUnAuthorized("Un Authorized");
			}

		} catch (ReminderNotFoundException e) {
			throw e;
		}
		catch (UserUnAuthorized e) {
			throw e;
		}
		return new ResponseEntity<Reminder>(reminder, HttpStatus.OK);
	}

}