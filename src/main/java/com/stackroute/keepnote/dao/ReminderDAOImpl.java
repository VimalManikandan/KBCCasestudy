package com.stackroute.keepnote.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.exception.ReminderAlreadyExistException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Reminder;
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
public class ReminderDAOImpl implements ReminderDAO {
	
	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	private EntityManager entityManager;
	
	
	@Autowired
	public ReminderDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * Create a new reminder
	 */

	public Reminder createReminder(Reminder reminder) throws ReminderAlreadyExistException {
		Session session = entityManager.unwrap(Session.class);
		Reminder rm=null;
		if(getRemindercheck(reminder.getReminderId()) == null){
			reminder.setReminderCreationDate(new Date());
			Integer id= (Integer) session.save(reminder);	
			rm=session.get(Reminder.class, id);
		}
		else {
			throw new ReminderAlreadyExistException("Reminder Already Exists");
		}
		return rm;
	}
	
	public Reminder getRemindercheck(int reminderId) {
		Session session = entityManager.unwrap(Session.class);
		Reminder reminder= session.get(Reminder.class, reminderId);
		return reminder;
	}
	
	/*
	 * Update an existing reminder
	 */

	public boolean updateReminder(Reminder reminder) throws ReminderNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Reminder r1 = new Reminder();
		r1.setReminderId(reminder.getReminderId());
		r1.setNotes(reminder.getNotes());
		r1.setReminderCreatedBy(reminder.getReminderCreatedBy());
		r1.setReminderCreationDate(reminder.getReminderCreationDate());
		r1.setReminderDescription(reminder.getReminderDescription());
		r1.setReminderName(reminder.getReminderName());
		r1.setReminderType(reminder.getReminderType());
		try {
			session.merge(r1);			
		} catch (Exception e) {
			throw new ReminderNotFoundException("Reminder Not Found");
		}

		return true;

	}

	/*
	 * Remove an existing reminder
	 */
	
	public boolean deleteReminder(int reminderId) throws ReminderNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("delete from Reminder where reminderId=:rmId");
		query.setParameter("rmId", reminderId);
		if (query.executeUpdate() > 0) {
			return true;
		} else
			throw new ReminderNotFoundException("Reminder Not Found");
	}

	/*
	 * Retrieve details of a specific reminder
	 */
	
	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Reminder reminder=session.get(Reminder.class, reminderId);
		if(reminder==null) {
			throw new ReminderNotFoundException("Not Found");
		}
		else {
			return reminder;
		}

	}

	/*
	 * Retrieve details of all reminders by userId
	 */
	
	public List<Reminder> getAllReminderByUserId(String userId) throws UserNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("from Reminder r where r.reminderCreatedBy=:uId");
		query.setParameter("uId", userId);
		List<Reminder> rList= query.getResultList();
		if(rList!=null)
			return rList;
		else {
			throw new UserNotFoundException("User Not Found");
		}

	}

}
