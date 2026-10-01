package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.Enum.BorrowingStatus;
import com.library.dto.borrowing.BorrowingBookResponse;
import com.library.dto.user.UserResponse;
import com.library.dto.user.UserStatusUpdateRequest;
import com.library.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final AdminService adminService;
	
	public AdminController(AdminService adminService)
	{
		this.adminService = adminService;
	}
	
	@GetMapping("/users")
	public List<UserResponse> getAllUser()
	{
		return adminService.getAllUsers();
	}
	
	@PatchMapping("/users/{userId}/status")
	public UserResponse updateUserStatus(@PathVariable Long userId, @RequestBody UserStatusUpdateRequest request)
	{ 
		return adminService.updateUserStatus(userId, request);
	}
	
	@GetMapping("/Borrowings")
	public List<BorrowingBookResponse> getAllborrowings(@RequestParam(required = false) BorrowingStatus status)
	{
		return adminService.getAllBorrowings(status);
	}
}
