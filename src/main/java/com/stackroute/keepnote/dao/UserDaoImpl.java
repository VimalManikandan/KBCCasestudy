package com.stackroute.keepnote.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.stackroute.keepnote.exception.UserAlreadyExistException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.exception.UserUnAuthorized;
import com.stackroute.keepnote.model.User;

/*
 * This class is implementing the UserDAO interface. This class has to be annotated with 
 * @Repository annotation.
 * @Repository - is an annotation that marks the specific class as a Data Access Object, 
 * thus clarifying it's role.
 * @Transactional - The transactional annotation itself defines the scope of a single database 
 * 					transaction. The database transaction happens inside the scope of a persistence 
 * 					context.  
 * */

@Repository
@Transactional
public class UserDaoImpl implements UserDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */


	private EntityManager entityManager;
	
	
	@Autowired
	public UserDaoImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * Create a new user
	 */

	public boolean registerUser(User user) throws Exception {
		Session session = entityManager.unwrap(Session.class);
		try {
			if (getUsercheck(user.getUserId()) == null) {
				session.save(user);
			} else {
				throw new UserAlreadyExistException("Already Exists");
			}
		}

		catch (Exception e) {
			throw e;
		}
		return true;

	}

	/*
	 * Update an existing user
	 */

	public boolean updateUser(User user) throws UserNotFoundException {

		Session session = entityManager.unwrap(Session.class);
		User u1 = new User();
		u1.setUserAddedDate(user.getUserAddedDate());
		u1.setUserId(user.getUserId());
		u1.setUserMobile(user.getUserMobile());
		u1.setUserName(user.getUserName());
		u1.setUserPassword(user.getUserPassword());
		try {
			if (getUsercheck(user.getUserId()) != null) {
				session.merge(u1);
			} else {
				throw new UserNotFoundException("Not Found");
			}
		} catch (Exception e) {
			throw e;
		}

		return true;

	}

	public User getUsercheck(String UserId) {
		Session session = entityManager.unwrap(Session.class);
		User user = session.get(User.class, UserId);
		return user;
	}

	/*
	 * Retrieve details of a specific user
	 */
	public User getUserById(String UserId) throws UserNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		User user = session.get(User.class, UserId);
		if (user != null) {
			return user;
		} else {
			throw new UserNotFoundException("Not Found");
		}

	}

	/*
	 * validate an user
	 */

	public boolean validateUser(String userId, String password) throws UserUnAuthorized {
		Session session = entityManager.unwrap(Session.class);
		if (getUsercheck(userId) == null) {
			throw new UserUnAuthorized("Not Authorized");
		} else {
			try {
				Query query = session.createQuery("from User u where u.userId=:uId and u.userPassword=:uPassword");
				query.setParameter("uId", userId);
				query.setParameter("uPassword", password);
				List uList = query.getResultList();
				if (uList.size() > 0)
					return true;
				else
					throw new UserUnAuthorized("Not Authorized");
			} catch (Exception e) {
				throw e;

			}
		}
	}

	/*
	 * Remove an existing user
	 */
	public boolean deleteUser(String userId) throws UserNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("delete from User where userId=:usrId");
		query.setParameter("usrId", userId);
		if (query.executeUpdate() > 0) {
			session.close();
			return true;
		} else
			session.close();
		throw new UserNotFoundException("UserNotFound");

	}

}
