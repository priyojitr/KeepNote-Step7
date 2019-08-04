package com.stackroute.keepnote.exception;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.apachecommons.CommonsLog;

@RestControllerAdvice
@CommonsLog
public class ExceptionHandlerAdvice {

	@ExceptionHandler({ ReminderNotCreatedException.class, ReminderNotFoundException.class })
	public ResponseEntity<ExceptionMessageResponse> handleControllerException(final HttpServletRequest request,
			final Throwable ex) {
		log.error("Exception type: " + ex.getClass());
		log.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(ExceptionMessageResponse.builder().message(ex.getMessage()).build());
	}

	@ExceptionHandler({ Exception.class, IOException.class })
	public ResponseEntity<ExceptionMessageResponse> handleGenericException(final HttpServletRequest request,
			final Throwable ex) {
		log.error("Exception type: " + ex.getClass());
		log.error(ex.getMessage());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(ExceptionMessageResponse.builder().message("some error occurred.").build());
	}
}
