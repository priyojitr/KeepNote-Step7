package com.stackroute.keepnote.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.authtoken.BearerToken;
import com.stackroute.keepnote.exception.UserAlreadyExistsException;
import com.stackroute.keepnote.exception.UserNotFoundException;
import com.stackroute.keepnote.model.LoginModel;
import com.stackroute.keepnote.model.User;
import com.stackroute.keepnote.service.UserAuthenticationService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.apachecommons.CommonsLog;

/**
 * This service api is responsible for user registration, login & authentication
 * check. The authenticated user will be provided with authorization token using
 * JWT, which should be used for authentication check. All calls to api should
 * come from web app, thereby CORS handling should be provided.
 * 
 * @author ubuntu
 *
 */

@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@CommonsLog
@RestController
public class UserAuthenticationController {
	// secret key configuration parameter
	private static final String JWT_KEY = "jwt.key";

	// JWT token time to live before expire
	private static final long TOKEN_TTL = 3600000L;

	@Autowired
	private Environment env;

	private final UserAuthenticationService authenticationService;

	@Autowired
	public UserAuthenticationController(UserAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	/**
	 * This api is responsible for user registration using
	 * {@code UserAuthenticationService}. There should not be check on authorization
	 * token. It should receive {@code User} data and store it in DB.
	 * 
	 * @param user
	 * @return
	 * @throws UserAlreadyExistsException
	 */
	@PostMapping("/register")
	public User registerUser(@RequestBody User user) throws UserAlreadyExistsException {
		try {
			log.info("register user...");
			this.authenticationService.saveUser(user);
		} catch (UserAlreadyExistsException e) {
			final String exMsg = e.getClass().getName() + "::" + e.getMessage();
			log.info(exMsg);
			throw new UserAlreadyExistsException(e.getMessage());
		}
		return user;
	}

	/**
	 * This is responsible for user login check based on user id & password as
	 * received. If user data is valid, then authentication token should be
	 * generated using JWT and is sent back as response.
	 * 
	 * @param user
	 * @return
	 * @throws UserNotFoundException
	 */
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public BearerToken userLogin(@RequestBody LoginModel userLogin) throws UserNotFoundException {
		log.info("user login..." + userLogin.toString());
		try {
			this.authenticationService.findByUserIdAndPassword(userLogin.getUserId(), userLogin.getUserPassword());
			return BearerToken.builder().token(createToken(userLogin.getUserId())).authentication(Boolean.TRUE).build();
		} catch (UserNotFoundException e) {
			log.info(e.getClass().getName() + "::" + e.getMessage());
			throw new UserNotFoundException(e.getMessage());
		}
	}

	/**
	 * This method is responsible to generate authorization token using JWT.
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public String createToken(String username) {
		// create token based on username as subjects
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("username", username);
		claims.setExpiration(new Date(System.currentTimeMillis() + TOKEN_TTL));
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256, env.getProperty(JWT_KEY)).compact();

	}

}
