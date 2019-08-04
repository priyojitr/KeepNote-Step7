package com.stackroute.keepnote.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Category {

	private String categoryId;
	private String categoryName;
	private String categoryDescription;
	private String categoryCreatedBy;
	private Date categoryCreationDate;
	
}