package com.library.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.dto.user.UserCreateRequest;
import com.library.dto.user.UserResponse;
import com.library.entity.User;
import com.library.exception.EmailAlreadyExistsException;
import com.library.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public UserResponse registerUser(UserCreateRequest request)
	{
		// check wheather email already exist
		if(userRepository.existsByEmail(request.getEmail()))
		{
			throw new EmailAlreadyExistsException("Email already exists!");
		}
		
		User user = new User();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		
		// Hash password before saving
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		//normal registration always create user
		user.setRole("USER");
		
		user.setActive(true);
		
		User savedUser = userRepository.save(user);
		
		return convertToResponse(savedUser);
	}
	
	 private UserResponse convertToResponse(User user) {

	        UserResponse response = new UserResponse();

	        response.setId(user.getId());
	        response.setName(user.getName());
	        response.setEmail(user.getEmail());
	        response.setRole(user.getRole());
	        response.setActive(user.isActive());

	        return response;
	    }
}
