package com.stackroute.keepnote.test.dao;

import static org.junit.Assert.*;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.stackroute.keepnote.config.ApplicationContextConfig;
import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.dao.NoteDAOImpl;
import com.stackroute.keepnote.exception.NotAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Note;

@RunWith(SpringRunner.class)
@Transactional
@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationContextConfig.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class NoteDAOImplTest {

	@Autowired
	private EntityManager entityManager;
	//private SessionFactory sessionFactory;
	private NoteDAO noteDAO;
	private Note note;

	@Before
	public void setUp() {
		noteDAO = new NoteDAOImpl(entityManager);
		note = new Note(1, "Testing-1", "Testing Service layer", "Active", "abc", new Date(), null, null, "Jhon123");

	}

	@After
	public void tearDown() throws Exception {
		Query query = entityManager.unwrap(Session.class).createQuery("DELETE from Note");
		query.executeUpdate();
	}

	@Test
	@Rollback(true)
	public void testCreateNoteSuccess() throws NotAlreadyExistsException, UserNotFoundException, NoteNotFoundException {

		noteDAO.createNote(note);
		List<Note> notes = noteDAO.getAllNotesByUserId("Jhon123");
		assertEquals("Testing-1", notes.get(0).getNoteTitle());
		noteDAO.deleteNote(note.getNoteId());
	}

	@Test
	@Rollback(true)
	public void testCreateNoteFailure() throws NotAlreadyExistsException, UserNotFoundException, NoteNotFoundException {

		noteDAO.createNote(note);
		List<Note> notes = noteDAO.getAllNotesByUserId("Jhon123");
		assertNotEquals("Testing-2", notes.get(0).getNoteTitle());
		noteDAO.deleteNote(note.getNoteId());

	}

	@Test
	@Rollback(true)
	public void testDeleteNoteSuccess() throws NoteNotFoundException, NotAlreadyExistsException {

		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(note.getNoteId());
		boolean status = noteDAO.deleteNote(noteData.getNoteId());
		assertEquals(true, status);

	}

	@Test
	public void testGetAllNotesByUserId() throws NotAlreadyExistsException, UserNotFoundException, NoteNotFoundException {
		Note note2 = new Note(2, "Testing-1", "Testing Service layer", "Active", "abc", new Date(), null, null, "Jhon123");
		Note note3 = new Note(3, "Testing-1", "Testing Service layer", "Active", "abc", new Date(), null, null, "Jhon123");
		noteDAO.createNote(note);
		noteDAO.createNote(note2);
		noteDAO.createNote(note3);
		List<Note> notes = noteDAO.getAllNotesByUserId("Jhon123");
		assertEquals(3, notes.size());
		noteDAO.deleteNote(note.getNoteId());
		noteDAO.deleteNote(note2.getNoteId());
		noteDAO.deleteNote(note3.getNoteId());
	}

	@Test
	@Rollback(true)
	public void testGetNoteById() throws NoteNotFoundException, NotAlreadyExistsException {

		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(note.getNoteId());
		assertEquals(note, noteData);
		noteDAO.deleteNote(note.getNoteId());

	}

	@Test(expected = NoteNotFoundException.class)
	@Rollback(true)
	public void testGetNoteByIdFailure() throws NoteNotFoundException, NotAlreadyExistsException {

		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(2);
		assertEquals(note, noteData);
		noteDAO.deleteNote(note.getNoteId());

	}

	@Test
	@Rollback(true)
	public void testUpdateNote() throws Exception {
		noteDAO.createNote(note);
		Note noteData = noteDAO.getNoteById(note.getNoteId());
		noteData.setContent("Unit testing for DAO layer");
		noteData.setCreatedAt(new Date());
		;
		boolean status = noteDAO.UpdateNote(noteData);
		Note updatedNote = noteDAO.getNoteById(noteData.getNoteId());
		assertEquals("Unit testing for DAO layer", updatedNote.getContent());
		assertEquals(true, status);
		noteDAO.deleteNote(updatedNote.getNoteId());

	}

}
