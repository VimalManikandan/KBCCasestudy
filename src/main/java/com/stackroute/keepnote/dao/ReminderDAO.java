package com.stackroute.keepnote.dao;

import java.util.List;

import com.stackroute.keepnote.exception.ReminderAlreadyExistException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Reminder;

public interface ReminderDAO {
	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */

	public Reminder createReminder(Reminder reminder) throws ReminderAlreadyExistException;

	public boolean updateReminder(Reminder reminder) throws ReminderNotFoundException;

	public boolean deleteReminder(int reminderId) throws ReminderNotFoundException;

	public Reminder getReminderById(int reminderId) throws ReminderNotFoundException;

	public List<Reminder> getAllReminderByUserId(String userId) throws UserNotFoundException;
}
