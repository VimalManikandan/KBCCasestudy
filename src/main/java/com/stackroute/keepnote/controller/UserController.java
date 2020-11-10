package com.stackroute.keepnote.controller;

import java.lang.annotation.Annotation;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.Responce;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working on creating RESTful web service, hence annotate
 * the class with @RestController annotation. A class annotated with the @Controller annotation
 * has handler methods which return a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */

@RestController
@RequestMapping("/user")
public class UserController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create an object using the new
	 * keyword
	 */

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/*
	 * Define a handler method which will create a specific user by reading the
	 * Serialized object from request body and save the user details in a User table
	 * in the database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 201(CREATED) - If the user created
	 * successfully. 2. 409(CONFLICT) - If the userId conflicts with any existing
	 * user
	 * 
	 * Note: ------ This method can be called without being logged in as well as
	 * when a new user will use the app, he will register himself first before
	 * login.
	 * 
	 * This handler method should map to the URL "/user/register" using HTTP POST
	 * method
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Responce> registerUser(@RequestBody User user) throws Exception {

		Responce responce = new Responce();

		try {
			userService.registerUser(user);
			responce.setMessage("CREATED");
			responce.setStatus(HttpStatus.CREATED.value());
		} catch (Exception e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce, HttpStatus.OK);
	}

	/*
	 * Define a handler method which will update a specific user by reading the
	 * Serialized object from request body and save the updated user details in a
	 * user table in database handle exception as well. This handler method should
	 * return any one of the status messages basis on different situations: 1.
	 * 200(OK) - If the user updated successfully. 2. 404(NOT FOUND) - If the user
	 * with specified userId is not found. 3. 401(UNAUTHORIZED) - If the user trying
	 * to perform the action has not logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP PUT method.
	 */

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String id,
			HttpServletRequest request) throws Exception {

		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, id) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}

		User user1=null;
		try {
			user1=userService.updateUser(user1, id);	
			return new ResponseEntity<User>(user1, HttpStatus.OK);
			
		} catch (UserNotFoundException e) {
			throw e;
		}
	}

	/*
	 * Define a handler method which will delete a user from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the user deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the user with specified userId is not found.
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/user/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid userId without {}
	 */

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<Responce> deleteUser(@PathVariable String id, HttpServletRequest request)
			throws UserNotFoundException, UserUnAuthorized {
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, id) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}

		Responce responce = new Responce();
		try {
			userService.deleteUser(id);
			responce.setMessage("OK");
			responce.setStatus(HttpStatus.OK.value());

		} catch (UserNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce, HttpStatus.OK);

	}

	/*
	 * Define a handler method which will show details of a specific user handle
	 * UserNotFoundException as well. This handler method should return any one of
	 * the status messages basis on different situations: 1. 200(OK) - If the user
	 * found successfully. 2. 401(UNAUTHORIZED) - If the user trying to perform the
	 * action has not logged in. 3. 404(NOT FOUND) - If the user with specified
	 * userId is not found. This handler method should map to the URL "/user/{id}"
	 * using HTTP GET method where "id" should be replaced by a valid userId without
	 * {}
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<User> getUser(@PathVariable String id,HttpSession session)
			throws UserNotFoundException, UserUnAuthorized {
		List<String> usersList = (List<String>) session.getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, id) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}
		User user=null;
		Responce responce = new Responce();
		try {
			user=userService.getUserById(id);			
		} catch (UserNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

}