package com.library.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.auth.LoginRequest;
import com.library.dto.auth.LoginResponse;
import com.library.dto.user.UserCreateRequest;
import com.library.dto.user.UserResponse;
import com.library.service.AuthService;
import com.library.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;
	private final AuthService authService;
	
	public AuthController(UserService userService, AuthService authService)
	{
		this.userService=userService;
		this.authService=authService;
	}
	
	@PostMapping("/register")
	public  UserResponse  registerUser(@Valid @RequestBody UserCreateRequest request)
	{
		return  userService.registerUser(request);
	}
	
	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request)
	{
		return authService.login(request);
	}
	
	
}
