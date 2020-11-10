package com.stackroute.keepnote.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.dao.UserDAO;
import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.User;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn�t currently 
* provide any additional behavior over the @Component annotation, but it�s a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */

@Service
public class UserServiceImpl implements UserService {

	/*
	 * Autowiring should be implemented for the userDAO. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword.
	 */

	/*
	 * This method should be used to save a new user.
	 * 
	 */
	UserDAO userDAO;

	@Autowired
	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public boolean registerUser(User user) throws Exception {
		return userDAO.registerUser(user);
	}

	/*
	 * This method should be used to update a existing user.
	 */

	public User updateUser(User user, String userId) throws UserNotFoundException {
		if (userDAO.updateUser(user))
			return userDAO.getUserById(userId);
		else
			return null;

	}

	/*
	 * This method should be used to get a user by userId.
	 */

	public User getUserById(String UserId) throws UserNotFoundException {
		return userDAO.getUserById(UserId);
	}

	/*
	 * This method should be used to validate a user using userId and password.
	 */

	public boolean validateUser(String userId, String password) throws UserUnAuthorized {
		return userDAO.validateUser(userId, password);
	}

	/* This method should be used to delete an existing user. */
	public boolean deleteUser(String UserId) throws UserNotFoundException {
		return userDAO.deleteUser(UserId);
	}

}
