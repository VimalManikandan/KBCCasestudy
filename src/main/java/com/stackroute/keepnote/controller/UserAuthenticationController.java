package com.stackroute.keepnote.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.Responce;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation.
 * Annotate class with @SessionAttributes this  annotation is used to store the model attribute in the session.
 */

@RestController
public class UserAuthenticationController {

	/*
	 * Autowiring should be implemented for the UserService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	UserService userService;

	@Autowired
	public UserAuthenticationController(UserService userService) {
		this.userService = userService;
	}
	
	/*
	 * Define a handler method which will authenticate a user by reading the
	 * Serialized user object from request body containing the userId and password
	 * and validating the same. Post login, the userId will have to be stored into
	 * session object, so that we can check whether the user is logged in for all
	 * other services handle UserNotFoundException as well. This handler method
	 * should return any one of the status messages basis on different situations:
	 * 1. 200(OK) - If login is successful. 2. 401(UNAUTHORIZED) - If login is not
	 * successful
	 * 
	 * This handler method should map to the URL "/login" using HTTP POST method
	 */
	
	@RequestMapping(value="/login"    ,method = RequestMethod.POST)
	public ResponseEntity<Responce> loginUser(@RequestBody User user,HttpServletRequest request) throws UserUnAuthorized {
		Responce responce=new Responce();
		try {
			if(userService.validateUser(user.getUserId(), user.getUserPassword())) {
			List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		       if (usersList == null) {
		        	usersList = new ArrayList<>();
		            request.getSession().setAttribute("Logged_users", usersList);
		        }
		        usersList.add(user.getUserId());
		        request.getSession().setAttribute("Logged_users", usersList);
		        responce.setMessage("Logged in");
		        responce.setStatus(HttpStatus.OK.value());
			}
			else {
				responce.setMessage("Login Failed");
				responce.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
			
		} catch (UserUnAuthorized e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce,HttpStatus.OK);
		
		
	}

	
	
	/*
	 * Define a handler method which will perform logout. Post logout, the user
	 * session is to be destroyed. This handler method should return any one of the
	 * status messages basis on different situations: 1. 200(OK) - If logout is
	 * successful 2. 400(BAD REQUEST) - If logout has failed
	 * 
	 * This handler method should map to the URL "/logout" using HTTP GET method
	 * 
	 */
	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public boolean logoutUser(HttpSession session) {
		try{
			session.invalidate();
			return true;
		}
		catch(Exception e) {
			return false;
		}	
	}

}
