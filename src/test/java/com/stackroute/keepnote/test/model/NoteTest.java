package com.stackroute.keepnote.test.model;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import com.stackroute.keepnote.model.Note;

public class NoteTest {

	private Note note;

	@Before
	public void setUp() throws Exception {
		note = new Note();
		note.setNoteId(1);
		note.setNoteTitle("Testing");
		note.setContent("Bean Testing");
		note.setCreatedBy("Jhon123");
		note.setCategory(null);
		note.setReminder(null);
		note.setNoteStatus("Active");
		note.setCreatedAt(new Date());
		note.setNote("Testing Note");
	}

	@Test
	public void Beantest() {
		new BeanTester().testBean(Note.class);

	}

}
