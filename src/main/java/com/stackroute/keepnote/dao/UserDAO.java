package com.stackroute.keepnote.dao;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.User;

public interface UserDAO {

	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */

	public boolean registerUser(User user) throws UserAlreadyExistException, Exception;

	public boolean updateUser(User user) throws UserNotFoundException;

	public User getUserById(String UserId) throws UserNotFoundException;

	public boolean validateUser(String userName, String password) throws  UserUnAuthorized ;

	public boolean deleteUser(String UserId) throws UserNotFoundException;
}
