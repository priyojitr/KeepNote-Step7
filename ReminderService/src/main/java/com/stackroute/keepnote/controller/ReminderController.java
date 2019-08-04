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

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.service.ReminderService;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@CommonsLog
public class ReminderController {

	private ReminderService reminderService;

	@Autowired
	public ReminderController(ReminderService reminderService) {
		this.reminderService = reminderService;
	}

	/**
	 * This api should be able to create a new reminder based on info received.
	 * 
	 * @throws ReminderNotCreatedException
	 */
	@PostMapping("/reminder")
	public Reminder createReminder(@RequestBody Reminder reminder) throws ReminderNotCreatedException {
		log.info("calling service layer to store");
		try {
			this.reminderService.createReminder(reminder);
			return reminder;
		} catch (ReminderNotCreatedException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new ReminderNotCreatedException(e.getMessage());
		}
	}

	/**
	 * This api should be able to delete a reminder based on reminder id.
	 * 
	 * @param userId
	 * @return
	 * @throws ReminderNotFoundException
	 */
	@GetMapping(value = "/reminder/delete/{reminderId}")
	public String deleteReminder(@PathVariable String reminderId) throws ReminderNotFoundException {
		try {
			this.reminderService.deleteReminder(reminderId);
			return "{\"isDeleted\":\"true\"}";
		} catch (ReminderNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new ReminderNotFoundException(e.getMessage());
		}

	}

	/**
	 * This api should be able to update a reminder info based on reminder id.
	 * 
	 * @param reminder
	 * @return
	 * @throws ReminderNotFoundException
	 */
	@PostMapping(value = "/reminder/update/{reminderId}")
	public Reminder updateReminder(@RequestBody Reminder reminder, @PathVariable String reminderId)
			throws ReminderNotFoundException {
		log.info("calling service layer to update");
		try {
			return this.reminderService.updateReminder(reminder, reminderId);
		} catch (ReminderNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	/**
	 * This api should return a specific reminder based on reminder id
	 * 
	 * @throws ReminderNotFoundException
	 */
	@GetMapping("/reminder/{userId}/{reminderId}")
	public Reminder getReminderById(@PathVariable String userId, @PathVariable String reminderId)
			throws ReminderNotFoundException {
		try {
			Reminder reminder = this.reminderService.getReminderById(reminderId);
			if (null != reminder) {
				return reminder;
			} else {
				throw new ReminderNotFoundException("reminder not found exception");
			}
		} catch (ReminderNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	/**
	 * This api should return list all reminders based on user id
	 * 
	 * @throws ReminderNotFoundException
	 */
	@GetMapping("/reminder/{userId}")
	public List<Reminder> getAllRemindersByUserId(@PathVariable String userId) throws ReminderNotFoundException {
		return this.reminderService.getAllReminders(userId);
	}
}
