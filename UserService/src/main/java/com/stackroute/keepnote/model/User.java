package com.stackroute.keepnote.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
@Document
public class User {

	@Id
	private String userId;
	private String firstName;
	private String lastName;
	private String userPassword;
	private String userRole;
	private Date userAddedDate;
}
