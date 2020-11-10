package com.stackroute.keepnote.dao;

import java.util.List;

import com.stackroute.keepnote.exception.NotAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.Note;

public interface NoteDAO {

	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */

	public boolean createNote(Note note) throws NotAlreadyExistsException;

	public boolean deleteNote(int noteId) throws NoteNotFoundException;

	public List<Note> getAllNotesByUserId(String userId) throws UserNotFoundException;

	public Note getNoteById(int noteId) throws NoteNotFoundException;

	public boolean UpdateNote(Note note) throws NoteNotFoundException, Exception;

}
