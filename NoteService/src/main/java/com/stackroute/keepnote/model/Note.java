package com.stackroute.keepnote.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder
public class Note {
	
	private int noteId;
	private String noteTitle;
	private String noteContent;
	private String noteStatus;
	private Date createdAt;
	private Category category;
	private List<Reminder> reminders;
	private String createdBy;
	
}
