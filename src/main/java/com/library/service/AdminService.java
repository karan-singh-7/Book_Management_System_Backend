package com.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.Enum.BorrowingStatus;
import com.library.dto.borrowing.BorrowingBookResponse;
import com.library.dto.user.UserResponse;
import com.library.dto.user.UserStatusUpdateRequest;
import com.library.entity.Borrowing;
import com.library.entity.User;
import com.library.exception.UserNotFoundException;
import com.library.repository.BorrowRepository;
import com.library.repository.UserRepository;

@Service
public class AdminService {

	private final UserRepository userRepository;
	private final BorrowRepository borrowRepository;
	
	public AdminService(UserRepository userRepository,BorrowRepository borrowRepository)
	{
		this.userRepository = userRepository;
		this.borrowRepository = borrowRepository;
	}
	
	public List<UserResponse> getAllUsers()
	{
		return userRepository.findAll()
				             .stream()
				             .map(this::convertToResponsse)
				             .toList();
	}
	
	public UserResponse updateUserStatus(Long id, UserStatusUpdateRequest request)
	{
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found!"));
		
		user.setActive(request.getActive());
		
		User savedUser = userRepository.save(user);
		return convertToResponsse(savedUser);
	}
	
	public List<BorrowingBookResponse> getAllBorrowings(BorrowingStatus status)
	{
	    List<Borrowing> borrowings;
	    
	    if(status == null)
	    {
	    	  borrowings = borrowRepository.findAll();
	    }
	    else
	    {
	    	  borrowings = borrowRepository.findByStatus(status);
	    }
		return  borrowings
	               .stream()
	               .map(this::convertToBorrowingResponse)
	               .toList();
	}
	
	private UserResponse convertToResponsse(User user)
	{
		 UserResponse response = new UserResponse();

	        response.setId(user.getId());
	        response.setName(user.getName());
	        response.setEmail(user.getEmail());
	        response.setRole(user.getRole());
	        response.setActive(user.isActive());

	        return response;
	}
	
	 private BorrowingBookResponse convertToBorrowingResponse( Borrowing borrowing) 
	 {

	        BorrowingBookResponse response = new BorrowingBookResponse();

	        response.setId(borrowing.getBorrowId());

	        response.setBookId(
	                borrowing.getBook().getId());

	        response.setBookTitle(
	                borrowing.getBook().getTitle());

	        response.setUserId(
	                borrowing.getUser().getId());

	        response.setUserName(
	                borrowing.getUser().getName());

	        response.setBorrowedAt(
	                borrowing.getBorrowedAt());

	        response.setReturnedAt(
	                borrowing.getReturnedAt());

	        response.setStatus(
	                borrowing.getStatus().name());

	        return response;
	    }
	
}
