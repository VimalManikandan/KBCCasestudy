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

import com.stackroute.keepnote.exception.CategoryAlreadyExistException;
import com.stackroute.keepnote.exception.CategoryNotFoundException;
import com.stackroute.keepnote.exception.NotAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Category;
import com.stackroute.keepnote.model.Note;

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
public class NoteDAOImpl implements NoteDAO {

	/*
	 * Autowiring should be implemented for the SessionFactory.(Use
	 * constructor-based autowiring.
	 */

	private EntityManager entityManager;
	
	
	@Autowired
	public NoteDAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * Create a new note
	 */
	
	public boolean createNote(Note note) throws NotAlreadyExistsException {
		Session session = entityManager.unwrap(Session.class);
		try {
			note.setCreatedAt(new Date());

			Integer id=  (Integer) session.save(note);	
			return true;
		}
		catch(Exception e) {
			throw new NotAlreadyExistsException ("Note Already Exists");
		}
	}
	
	/*
	 * public Note getNotecheck(int noteId) { Session session =
	 * entityManager.unwrap(Session.class); Note note= session.get(Note.class,
	 * noteId); return note; }
	 */

	/*
	 * Remove an existing note
	 */
	
	public boolean deleteNote(int noteId) throws NoteNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("delete from Note where noteId=:nId");
		query.setParameter("nId", noteId);
		try {
			if (query.executeUpdate() > 0) {
				return true;
			} else
				throw new NoteNotFoundException("Note Not Found");
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Retrieve details of all notes by userId
	 */
	
	public List<Note> getAllNotesByUserId(String userId) throws UserNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("from Note n where n.createdBy=:uId");
		query.setParameter("uId", userId);
		List<Note> nList= query.getResultList();
		if(nList!=null)
			return nList;
		else {
			throw new UserNotFoundException("User Not Found");
		}

	}

	/*
	 * Retrieve details of a specific note
	 */
	
	public Note getNoteById(int noteId) throws NoteNotFoundException {
		Session session = entityManager.unwrap(Session.class);
		Note note=session.get(Note.class, noteId);
		try {
		if(note==null) {
			throw new NoteNotFoundException("Not Found");
		}
		else {
			return note;
		}
		}
		catch(NoteNotFoundException e) {
			throw e;
		}
		catch(Exception e) {
			throw e;
		}

	}

	/*
	 * Update an existing note
	 */

	public boolean UpdateNote(Note note) throws Exception {
		Session session = entityManager.unwrap(Session.class);
		Note n1=new Note();
		n1.setCategory(note.getCategory());
		n1.setContent(note.getContent());
		n1.setCreatedAt(note.getCreatedAt());
		n1.setCreatedBy(note.getCreatedBy());
		n1.setNote(note.getNote());
		n1.setNoteId(note.getNoteId());
		n1.setNoteStatus(note.getNoteStatus());
		n1.setNoteTitle(note.getNoteTitle());
		n1.setReminder(note.getReminder());		
		try {
			session.merge(n1);	
			return true;
		} catch (Exception e) {
			throw new NoteNotFoundException("Note Not Found");		
		}
	}


}
