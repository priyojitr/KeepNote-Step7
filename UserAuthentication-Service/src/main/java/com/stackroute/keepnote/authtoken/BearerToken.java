package com.stackroute.keepnote.authtoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BearerToken {

	private String token;
	private boolean authentication;

}
