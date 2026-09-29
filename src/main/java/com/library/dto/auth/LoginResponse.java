package com.library.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

	 private String token;
	 private String tokenType;
	 private Long userId;
	 private String name;
	 private String email;
	 private String role;

}
