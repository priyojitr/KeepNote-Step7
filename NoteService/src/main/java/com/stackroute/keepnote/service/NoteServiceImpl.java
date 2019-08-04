package com.stackroute.keepnote.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.NoteAlreadyExistsException;
import com.stackroute.keepnote.exception.NoteNotFoundExeption;
import com.stackroute.keepnote.model.Note;
import com.stackroute.keepnote.model.NoteUser;
import com.stackroute.keepnote.repository.NoteRepository;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class NoteServiceImpl implements NoteService {

	private final NoteRepository noteRepository;

	@Autowired
	public NoteServiceImpl(NoteRepository noteRepository) {
		this.noteRepository = noteRepository;
	}

	public boolean createNote(Note note) throws NoteAlreadyExistsException {
		log.info("create new note service");
		List<Note> noteList = null;
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(note.getCreatedBy())).get();
			if (noteUser.isPresent()) {
				noteList = noteUser.get().getNotes();
				if (noteList.stream().anyMatch(currNote -> currNote.getNoteId() == note.getNoteId())) {
					throw new NoteAlreadyExistsException("note already exists exception");
				}
				note.setCreatedAt(new Date());
				noteList.add(note);
			} else {
				noteList = Arrays.asList(note);
			}
			note.setCreatedAt(new Date());
			this.noteRepository.save(NoteUser.builder().userId(note.getCreatedBy()).notes(noteList).build());
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new NoteAlreadyExistsException(e.getMessage());
		}
	}

	public boolean deleteNote(String userId, int noteId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (!noteUser.isPresent()) {
				throw new NoteNotFoundExeption("note not found exception");
			} else {
				List<Note> filteredNotes = noteUser.get().getNotes().stream()
						.filter(currNote -> currNote.getNoteId() != noteId).collect(Collectors.toList());
				noteUser.get().setNotes(filteredNotes);
				this.noteRepository.save(noteUser.get());
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}

	}

	public boolean deleteAllNotes(String userId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> notes = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (notes.isPresent()) {
				this.noteRepository.delete(notes.get());
				return Boolean.TRUE;
			} else {
				throw new NoteNotFoundExeption("note not found exception");
			}
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

	public Note updateNote(Note note, int id, String userId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (noteUser.isPresent()) {
				List<Note> updatedNoteList = noteUser.get().getNotes().stream()
						.filter(currNote -> currNote.getNoteId() != id).collect(Collectors.toList());
				updatedNoteList.add(note);
				noteUser.get().setNotes(updatedNoteList);
				this.noteRepository.save(noteUser.get());
				return note;
			} else {
				throw new NoteNotFoundExeption("note not found exception");
			}
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

	public Note getNoteByNoteId(String userId, int noteId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if (noteUser.isPresent()) {
				Optional<Note> noteOptional = noteUser.get().getNotes().stream()
						.filter(currNote -> currNote.getNoteId() == noteId).findFirst();
				if (noteOptional.isPresent()) {
					return noteOptional.get();
				} else {
					throw new NoteNotFoundExeption("note not found exception");
				}
			} else {
				throw new NoteNotFoundExeption("note not found exception -- userid");
			}
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption(e.getMessage());
		}
	}

	public List<Note> getAllNoteByUserId(String userId) throws NoteNotFoundExeption {
		try {
			Optional<NoteUser> noteUser = Optional.ofNullable(this.noteRepository.findById(userId)).get();
			if(noteUser.isPresent()) {
				return noteUser.get().getNotes();
			}else {
				return Collections.<Note>emptyList();
			}
			
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new NoteNotFoundExeption("note by user id not found");
		}

	}

}
