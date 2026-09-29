package com.library.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.dto.auth.LoginRequest;
import com.library.dto.auth.LoginResponse;
import com.library.entity.User;
import com.library.repository.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	private final JwtService jwtService;
	
	public AuthService(UserRepository userRepository, BCryptPasswordEncoder encoder, JwtService jwtService) {
		super();
		this.userRepository = userRepository;
		this.encoder = encoder;
		this.jwtService = jwtService;
	}
	
	public LoginResponse login(LoginRequest request)
	{
		System.out.println("Email received: [" + request.getEmail() + "]");
	    System.out.println("Password received: [" + request.getPassword() + "]");
	    
		User user = userRepository.findByEmail(request.getEmail())
		              .orElseThrow(() -> new RuntimeException("Invalid Email or Password!"));
		
		System.out.println("User name: "+user.getName());
		
		// check user is active or not
		if(!user.isActive())
		{
			throw new RuntimeException("User account is inactive!");
		}
		System.out.println("User Password: "+user.getPassword());
		System.out.println("Request Passeord: "+request.getPassword());
		// check password
		boolean passwordMatches = encoder.matches(request.getPassword(), user.getPassword());
		
		System.out.println("Password Matches: "+passwordMatches);
		
		if(!passwordMatches)
		{
			throw new RuntimeException("Invalid Email or Password!");
		}
		
		// generate jwt
		String token = jwtService.generateToken(user.getId(), user.getEmail(), user.getRole());
		
		// create response
		
		LoginResponse response = new LoginResponse();
		response.setEmail(user.getEmail());
		response.setName(user.getName());
		response.setRole(user.getRole());
		response.setToken(token);
		response.setUserId(user.getId());
		response.setTokenType("Bearer");
		
		return response;
	}
}
