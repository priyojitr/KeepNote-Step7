package com.stackroute.keepnote.service;

import com.stackroute.keepnote.exception.NoteAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;

import java.util.List;


public interface NoteService {
	
	/*
	 * Should not modify this interface. You have to implement these methods in
	 * corresponding Impl classes
	 */


    boolean createNote(Note note) throws NoteAlreadyExistsException;

    boolean deleteNote(String userId, int noteId) throws NoteNotFoundExeption;

    boolean deleteAllNotes(String userId) throws NoteNotFoundExeption;

    Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption;

    Note getNoteByNoteId(String userId,int noteId) throws NoteNotFoundExeption;

    List<Note> getAllNoteByUserId(String userId) throws NoteNotFoundExeption;


}
