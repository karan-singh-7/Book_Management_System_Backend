package com.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.borrowing.BorrowBookRequest;
import com.library.dto.borrowing.BorrowingBookResponse;
import com.library.entity.User;
import com.library.repository.UserRepository;
import com.library.service.BorrowingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/borrowing")
public class BorrowingController {

	private final BorrowingService borrowService;
	private final UserRepository userRepository;
	
	public BorrowingController(BorrowingService borrowService, UserRepository userRepository)
	{
		this.borrowService=borrowService;
		this.userRepository=userRepository;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BorrowingBookResponse borrowBook(@Valid @RequestBody BorrowBookRequest request, Authentication authentication)
	{
		String email = authentication.getName();
		
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!"));
		
		return borrowService.borrowBook(user.getId(), request);
	}
	
	@PatchMapping("{bookId}/returned")
	public BorrowingBookResponse returnBook(@PathVariable Long bookId, Authentication authentication)
	{
		String email = authentication.getName();
		
		return borrowService.returnBook(bookId, email);
	}
	
	@GetMapping("/my")
	public List<BorrowingBookResponse> getMyBorrowingHistory(Authentication authentication)
	{
		String email = authentication.getName();
		return borrowService.getMyBorrowingHistory(email);
	}
	
	@GetMapping("/my/active")
	public List<BorrowingBookResponse> getActiveBorrowings(Authentication authentication)
	{
		String email = authentication.getName();
		return borrowService.getMyActiveBorrowings(email);
	}
}
