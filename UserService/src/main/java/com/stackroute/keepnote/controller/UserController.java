package com.stackroute.keepnote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserService;

import lombok.extern.apachecommons.CommonsLog;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@CommonsLog
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * This api is used to store user entity into DB, which should persist for user
	 * login.
	 * 
	 * @param user
	 * @return
	 * @throws UserAlreadyExistsException
	 */
	@PostMapping(value = "/user/register")
	public User registerUser(@RequestBody User user) throws UserAlreadyExistsException {
		log.info("calling service layer to store");
		try {
			this.userService.registerUser(user);
		} catch (UserAlreadyExistsException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserAlreadyExistsException(e.getMessage());
		}
		return user;
	}

	/**
	 * This api is used to update user entity into DB, which should persist for user
	 * login.
	 * 
	 * @param user
	 * @return
	 * @throws UserNotFoundException
	 */
	@PostMapping(value = "/user/update/{userid}")
	public User updateUser(@RequestBody User user, @PathVariable("userid") String userId) throws UserNotFoundException {
		log.info("calling service layer to update");
		try {
			return this.userService.updateUser(userId, user);
		} catch (UserNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
	}

	@PostMapping(value = "/user/delete/{userid}")
	public String deleteUser(@PathVariable("userid") String userId) throws UserNotFoundException {
		try {
			this.userService.deleteUser(userId);
			return "{\"isDeleted\":\"true\"}";
		} catch (UserNotFoundException e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}

	}

	@GetMapping("/user/{userid}")
	public User getUser(@PathVariable("userid") String userId) throws UserNotFoundException {
		try {
			User user=this.userService.getUserById(userId);
			if (null != user) {
				return user;
			}else {
				throw new UserNotFoundException("user not found exception");
			}
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
	}
}
