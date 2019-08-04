package com.stackroute.keepnote.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.ReminderNotCreatedException;
import com.stackroute.keepnote.exception.ReminderNotFoundException;
import com.stackroute.keepnote.model.Reminder;
import com.stackroute.keepnote.repository.ReminderRepository;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class ReminderServiceImpl implements ReminderService {

	private final ReminderRepository reminderRepository;

	public ReminderServiceImpl(ReminderRepository reminderRepository) {
		this.reminderRepository = reminderRepository;
	}

	public Reminder createReminder(Reminder reminder) throws ReminderNotCreatedException {
		try {
			reminder.setReminderCreationDate(new Date());
			return this.reminderRepository.insert(reminder);
		} catch (Exception e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new ReminderNotCreatedException(e.getMessage());
		}
	}

	public boolean deleteReminder(String reminderId) throws ReminderNotFoundException {
		try {
			Optional<Reminder> reminder = Optional.ofNullable(this.reminderRepository.findById(reminderId)).get();
			if (!reminder.isPresent()) {
				throw new ReminderNotFoundException("reminder does not exists exception");
			}
			this.reminderRepository.delete(reminder.get());
			return Boolean.TRUE;
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	public Reminder updateReminder(Reminder reminder, String reminderId) throws ReminderNotFoundException {
		try {
			Optional<Reminder> reminderOptional = Optional.ofNullable(this.reminderRepository.findById(reminderId))
					.get();
			if (!reminderOptional.isPresent()) {
				throw new ReminderNotFoundException("reminder not found exception");
			}
			this.reminderRepository.save(reminder);
			return this.reminderRepository.findById(reminderId).get();
		} catch (ReminderNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	public Reminder getReminderById(String reminderId) throws ReminderNotFoundException {
		try {
			Optional<Reminder> reminder = Optional.ofNullable(this.reminderRepository.findById(reminderId)).get();
			if (!reminder.isPresent()) {
				throw new ReminderNotFoundException("reminder not found exception");
			}
			return reminder.get();
		} catch (ReminderNotFoundException e) {
			log.info(e.getClass() + " -- " + e.getMessage());
			throw new ReminderNotFoundException(e.getMessage());
		}
	}

	public List<Reminder> getAllReminders(String userId) {
		return this.reminderRepository.findAllReminderByReminderCreatedBy(userId);
	}

}
