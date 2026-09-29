package com.library.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {

	@NotNull(message = "Email is required!")
	@Email(message = "Enter a valid Email!")
	private String email;
	
	@NotNull(message = "Password is required!")
	private String password;
	
}
