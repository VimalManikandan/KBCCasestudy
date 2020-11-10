package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.User;

public interface UserService {
	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */
	public boolean registerUser(User user) throws UserAlreadyExistException, Exception;

	public User updateUser(User user, String id) throws Exception;

	public boolean deleteUser(String UserId) throws UserNotFoundException;

	public boolean validateUser(String userId, String password) throws  UserUnAuthorized;

	public User getUserById(String userId) throws UserNotFoundException;

}
