package com.stackroute.keepnote.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
public class User {

	@Id
	private String userId;
	private String firstName;
	private String lastName;
	private String userPassword;
	private String userRole;
	private Date userAddedDate;

}
