package com.stackroute.keepnote.test.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.stackroute.keepnote.dao.ReminderDAO;
import com.stackroute.keepnote.exception.ReminderAlreadyExistException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.ReminderServiceImpl;

public class ReminderServiceImplTest {

	@Mock
	ReminderDAO reminderDAO;
	@InjectMocks
	ReminderServiceImpl reminderServiceImpl;
	private Reminder reminder = null;
	private List<Reminder> allReminder = null;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		reminder = new Reminder(1, "Email", "Email reminder", "EmailType", "Jhon123", new Date(), null);
		allReminder = new ArrayList<Reminder>();
	}

	@Test
	public void testCreateReminderSuccess() throws ReminderAlreadyExistException {

		when(reminderDAO.createReminder(reminder)).thenReturn(reminder);
		Reminder rm = reminderServiceImpl.createReminder(reminder);
		assertNotNull(rm);
		verify(reminderDAO, times(1)).createReminder(reminder);
	}

	@Test
	public void testCreateReminderFailure() throws ReminderAlreadyExistException {

		when(reminderDAO.createReminder(reminder)).thenReturn(reminder);
		Reminder r1 = reminderServiceImpl.createReminder(reminder);
		assertNull(r1);
		verify(reminderDAO, times(1)).createReminder(reminder);
	}

	@Test
	public void testUpdateReminderSuccess() throws ReminderNotFoundException {

		when(reminderDAO.getReminderById(reminder.getReminderId())).thenReturn(reminder);
		reminder.setReminderDescription("Notification Reminder");
		when(reminderDAO.updateReminder(reminder)).thenReturn(true);
		@SuppressWarnings("unused")
		Reminder updatedReminder = reminderServiceImpl.updateReminder(reminder, 1);
		assertEquals("Notification Reminder", reminder.getReminderDescription());
		verify(reminderDAO, times(1)).getReminderById(reminder.getReminderId());
		verify(reminderDAO, times(1)).updateReminder(reminder);
	}

	@Test(expected = ReminderNotFoundException.class)
	public void testUpdateReminderFailure() throws ReminderNotFoundException {

		when(reminderDAO.getReminderById(reminder.getReminderId())).thenThrow(ReminderNotFoundException.class);
		when(reminderDAO.updateReminder(reminder)).thenReturn(true);
		@SuppressWarnings("unused")
		Reminder updatedReminder = reminderServiceImpl.updateReminder(reminder, 1);

	}

	@Test
	public void testDeleteReminder() throws ReminderNotFoundException {

		when(reminderDAO.deleteReminder(reminder.getReminderId())).thenReturn(true);
		boolean status = reminderServiceImpl.deleteReminder(reminder.getReminderId());
		assertEquals(true, status);
		verify(reminderDAO, times(1)).deleteReminder(reminder.getReminderId());
	}

	@Test
	public void testGetReminderByIdSuccess() throws ReminderNotFoundException {
		when(reminderDAO.getReminderById(reminder.getReminderId())).thenReturn(reminder);
		Reminder fetchedReminder = reminderServiceImpl.getReminderById(reminder.getReminderId());
		assertEquals(reminder, fetchedReminder);
		verify(reminderDAO, times(1)).getReminderById(reminder.getReminderId());
	}
	
	@Test(expected= ReminderNotFoundException.class)
	public void testGetReminderByIdFailure() throws ReminderNotFoundException {
		when(reminderDAO.getReminderById(reminder.getReminderId())).thenThrow(ReminderNotFoundException.class);
		@SuppressWarnings("unused")
		Reminder fetchedReminder = reminderServiceImpl.getReminderById(reminder.getReminderId());
		
	}

	@Test
	public void testGetAllReminder() throws UserNotFoundException {
		allReminder.add(reminder);
		reminder = new Reminder(2, "Email", "Email reminder", "EmailType", "Jhon123", new Date(), null);
		allReminder.add(reminder);
		reminder = new Reminder(3, "Email", "Email reminder", "EmailType", "Jhon123", new Date(), null);
		allReminder.add(reminder);
		
		when(reminderDAO.getAllReminderByUserId("Jhon123")).thenReturn(allReminder);
		List<Reminder> reminders = reminderServiceImpl.getAllReminderByUserId("Jhon123");
		assertEquals(allReminder, reminders);
	}

}
