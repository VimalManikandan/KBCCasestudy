package com.stackroute.keepnote.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.CategoryAlreadyExistException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.model.Responce;
import com.stackroute.keepnote.service.CategoryService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

	/*
	 * Autowiring should be implemented for the CategoryService. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword
	 */
	private CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService=categoryService;
	}

	/*
	 * Define a handler method which will create a category by reading the
	 * Serialized category object from request body and save the category in
	 * category table in database. Please note that the careatorId has to be unique
	 * and the loggedIn userID should be taken as the categoryCreatedBy for the
	 * category. This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED - In case of successful
	 * creation of the category 2. 409(CONFLICT) - In case of duplicate categoryId
	 * 3. 401(UNAUTHORIZED) - If the user trying to perform the action has not
	 * logged in.
	 * 
	 * This handler method should map to the URL "/category" using HTTP POST
	 * method".
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Category> createCategory(@RequestBody Category category,HttpServletRequest request) throws Exception {
		
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, category.getCategoryCreatedBy()) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("Un Authorized");
		}
		
		Category c1=null;	
		try {
			c1=categoryService.createCategory(category);
			if(c1!=null) {
				return new ResponseEntity<Category>(c1, HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Category>(c1, HttpStatus.BAD_REQUEST);				
			}
		}
		catch(CategoryAlreadyExistException e) {
			throw e;
		}
		
	}
	
	
	/*
	 * Define a handler method which will delete a category from a database.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the category with specified categoryId is
	 * not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/category/{id}" using HTTP Delete
	 * method" where "id" should be replaced by a valid categoryId without {}
	 */
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<Responce> deleteCategory(@PathVariable int id, HttpServletRequest request)
			throws UserUnAuthorized, CategoryNotFoundException {
		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		
		int rslt = (usersList != null) ? Collections.frequency(usersList, categoryService.getCategoryById(id).getCategoryCreatedBy()) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}

		Responce responce = new Responce();
		try {
			categoryService.deleteCategory(id);
			responce.setMessage("OK");
			responce.setStatus(HttpStatus.OK.value());

		} catch (CategoryNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<Responce>(responce, HttpStatus.OK);

	}
	

	/*
	 * Define a handler method which will update a specific category by reading the
	 * Serialized object from request body and save the updated category details in
	 * a category table in database handle CategoryNotFoundException as well. please
	 * note that the loggedIn userID should be taken as the categoryCreatedBy for
	 * the category. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the category updated
	 * successfully. 2. 404(NOT FOUND) - If the category with specified categoryId
	 * is not found. 3. 401(UNAUTHORIZED) - If the user trying to perform the action
	 * has not logged in.
	 * 
	 * This handler method should map to the URL "/category/{id}" using HTTP PUT
	 * method.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Category> updateCategory(@RequestBody Category category, @PathVariable int id,
			HttpServletRequest request) throws Exception {

		List<String> usersList = (List<String>) request.getSession().getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList,categoryService.getCategoryById(id).getCategoryCreatedBy() ) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("UnAuthorized");
		}

		Category category2=null;
		try {
			category2=categoryService.updateCategory(category, id);
			if (category2 != null) {
				return new ResponseEntity<Category>(category2, HttpStatus.OK);
			} else {
				return new ResponseEntity<Category>(category2, HttpStatus.BAD_REQUEST);
			}
		} catch (CategoryNotFoundException e) {
			throw e;
		}
		
	}
	

	/*
	 * Define a handler method which will get us the category by a userId.
	 * 
	 * This handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the category found successfully. 2.
	 * 401(UNAUTHORIZED) -If the user trying to perform the action has not logged
	 * in.
	 * 
	 * 
	 * This handler method should map to the URL "/category" using HTTP GET method
	 */
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Category>> getCategoryrByUserId(@RequestParam String id, HttpSession session)
			throws UserUnAuthorized, UserNotFoundException {
		Responce responce = new Responce();
		List<String> usersList = (List<String>) session.getAttribute("Logged_users");
		int rslt = (usersList != null) ? Collections.frequency(usersList, id) : 0;
		if (rslt == 0) {
			throw new UserUnAuthorized("Un Authorized");
		}
		List<Category> categories=null;
		try {
			categories= categoryService.getAllCategoryByUserId(id);
			responce.setMessage("OK");
			responce.setStatus(HttpStatus.OK.value());
		} catch (UserNotFoundException e) {
			throw e;
		}
		return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
	}
	

}