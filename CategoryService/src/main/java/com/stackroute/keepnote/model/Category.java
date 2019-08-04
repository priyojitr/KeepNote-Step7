package com.stackroute.keepnote.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@Document
public class Category {

	@Id
	private String categoryId;
	private String categoryName;
	private String categoryDescription;
	private String categoryCreatedBy;
	private Date categoryCreationDate;

}
