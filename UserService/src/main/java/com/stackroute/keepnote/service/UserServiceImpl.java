package com.stackroute.keepnote.service;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stackroute.keepnote.exceptions.UserAlreadyExistsException;
import com.stackroute.keepnote.exceptions.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserRepository;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * This api is used to create/register new user. This api makes a call to user
	 * authentication service to store new user info for authentication purpose.
	 */
	public User registerUser(User user) throws UserAlreadyExistsException {
		User userCreated = null;
		try {
			Optional<User> userOptional = Optional.ofNullable(this.userRepository.findById(user.getUserId())).get();
			if (userOptional.isPresent()) {
				throw new UserAlreadyExistsException("user already exists exception");
			}
			user.setUserAddedDate(new Date());
			userCreated = this.userRepository.insert(user);
			RestTemplate template = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(user), headers);
			template.postForEntity("http://localhost:8089/api/v1/auth/register", entity, User.class);
		} catch (UserAlreadyExistsException | IOException | RestClientException e) {
			log.info(e.getClass().getName() + " :: " + e.getMessage());
			throw new UserAlreadyExistsException(e.getMessage());
		}
		return userCreated;
	}

	/**
	 * This api is used to update an existing user info.
	 */
	public User updateUser(String userId, User user) throws UserNotFoundException {
		try {
			Optional.ofNullable(this.userRepository.findById(userId)).get()
					.orElseThrow(() -> new UserNotFoundException("user not found exception"));
			this.userRepository.save(user);
			return this.userRepository.findById(user.getUserId()).get();
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
	}

	/**
	 * This api used to delete an existing user info.
	 */
	public boolean deleteUser(String userId) throws UserNotFoundException {
		try {
			User user = Optional.ofNullable(this.userRepository.findById(userId)).get()
					.orElseThrow(() -> new UserNotFoundException("user not found exception"));
			if (null != user) {
				this.userRepository.delete(user);
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
		return Boolean.FALSE;

	}

	/**
	 * This api is used to fetch user info based on user id
	 */
	public User getUserById(String userId) throws UserNotFoundException {
		try {
			Optional<User> user = Optional.ofNullable(this.userRepository.findById(userId)).get();
			if (user.isPresent()) {
				return user.get();
			} else {
				throw new UserNotFoundException("user not found exception");
			}
		} catch (Exception e) {
			log.info(e.getClass().getName() + " -- " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
	}
}
