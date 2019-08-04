package com.stackroute.keepnote.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.repository.UserAutheticationRepository;

import lombok.extern.apachecommons.CommonsLog;

@Service
@CommonsLog
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	private final UserAutheticationRepository userAuthRepo;

	@Autowired
	public UserAuthenticationServiceImpl(UserAutheticationRepository userAuthRepo) {
		this.userAuthRepo = userAuthRepo;
	}

	@Override
	public User findByUserIdAndPassword(String userId, String password) throws UserNotFoundException {
		User user = null;
		try {
			user = Optional.ofNullable(this.userAuthRepo.findByUserIdAndUserPassword(userId, password))
					.orElseThrow(() -> new UserNotFoundException("user not found exception"));
		} catch (Exception e) {
			log.error(e.getClass().getName() + " :: " + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
		return user;
	}

	@Override
	public boolean saveUser(User user) throws UserAlreadyExistsException {
		try {
			Optional<User> userOptional = this.userAuthRepo.findById(user.getUserId());
			if (userOptional.isPresent()) {
				throw new UserAlreadyExistsException("user already exists");
			} else {
				Optional.ofNullable(this.userAuthRepo.save(user))
						.orElseThrow(() -> new UserAlreadyExistsException("unable to register"));
			}
		} catch (UserAlreadyExistsException e) {
			log.error(e.getClass().getName() + " :: " + e.getMessage());
			throw new UserAlreadyExistsException(e.getMessage());
		}
		return Boolean.TRUE;
	}
}
