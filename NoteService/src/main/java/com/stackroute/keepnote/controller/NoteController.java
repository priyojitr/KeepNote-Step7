package com.stackroute.keepnote.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exception.NoteAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.service.NoteService;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@CommonsLog
public class NoteController {

	private NoteService noteService;

	@Autowired
	public NoteController(NoteService noteService) {
		this.noteService = noteService;
	}

	/**
	 * This api should be able to create a new based on info received.
	 * 
	 * @throws NoteAlreadyExistsException
	 */
	@PostMapping("/note")
	public Note createNote(@RequestBody Note note) throws NoteAlreadyExistsException {
		log.info("calling service layer to store");
		try {
			this.noteService.createNote(note);
			return note;
		} catch (NoteAlreadyExistsException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new NoteAlreadyExistsException(e.getMessage());
		}
	}

	/**
	 * This api should be able to delete a note based on user id & note id.
	 * 
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	@GetMapping(value = "/note/delete/{userId}/{noteId}")
	public String deleteNote(@PathVariable String userId, @PathVariable int noteId) throws NoteNotFoundExeption {
		try {
			this.noteService.deleteNote(userId, noteId);
			return "{\"isDeleted\":\"true\"}";
		} catch (NoteNotFoundExeption e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}

	}

	/**
	 * This api should be able to update a note info based on user id & note id.
	 * 
	 * @param userId
	 * @param noteId
	 * @return
	 * @throws NoteNotFoundExeption
	 */
	@PostMapping(value = "/note/update/{userId}/{noteId}")
	public Note updateNote(@RequestBody Note note, @PathVariable String userId, @PathVariable int noteId)
			throws NoteNotFoundExeption {
		log.info("calling service layer to update");
		try {
			return this.noteService.updateNote(note, noteId, userId);
		} catch (NoteNotFoundExeption e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

	/**
	 * This api should return list all notes based on user id
	 * 
	 * @throws NoteNotFoundExeption
	 */
	@GetMapping("/note/{userId}")
	public List<Note> getAllNotesByUserId(@PathVariable String userId) throws NoteNotFoundExeption {
			return this.noteService.getAllNoteByUserId(userId);
	}

	/**
	 * This api should return a specific note based on note id & user id
	 * 
	 * @throws NoteNotFoundExeption
	 */
	@GetMapping("/note/{userId}/{noteId}")
	public Note getNote(@PathVariable String userId, @PathVariable int noteId) throws NoteNotFoundExeption {
		try {
			Note note = this.noteService.getNoteByNoteId(userId, noteId);
			if (null != note) {
				return note;
			} else {
				throw new NoteNotFoundExeption("note not found exception");
			}
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

}
