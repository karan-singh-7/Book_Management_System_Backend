package com.library.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.Enum.BorrowingStatus;
import com.library.dto.admin.DashboardDto;
import com.library.dto.borrowing.BorrowingBookResponse;
import com.library.dto.user.UserResponse;
import com.library.dto.user.UserStatusUpdateRequest;
import com.library.entity.Book;
import com.library.entity.Borrowing;
import com.library.entity.User;
import com.library.exception.BookNotFoundException;
import com.library.exception.BorrowingNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.UserRepository;

@Service
public class AdminService {

	private final UserRepository userRepository;
	private final BorrowRepository borrowRepository;
	private final BookRepository bookRepository;
	
	public AdminService(UserRepository userRepository,BorrowRepository borrowRepository, BookRepository bookRepository)
	{
		this.userRepository = userRepository;
		this.borrowRepository = borrowRepository;
		this.bookRepository = bookRepository;
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
	
	public Page<BorrowingBookResponse> getAllBorrowings(BorrowingStatus status, int page, int size)
	{
		Pageable pageable  = PageRequest.of(page, size);
	    Page<Borrowing> borrowings;
	    
	    if(status == null)
	    {
	    	  borrowings = borrowRepository.findAll(pageable);
	    }
	    else
	    {
	    	  borrowings = borrowRepository.findByStatus(status, pageable);
	    }
	    return borrowings.map(this::convertToBorrowingResponse);
	}
	
	@Transactional
	public BorrowingBookResponse forceReturnBook(Long borrowingId)
	{
		Borrowing borrow = borrowRepository.findByIdForUpdate(borrowingId)
		                .orElseThrow(() -> new BorrowingNotFoundException("Borrowings not found with this id: "+borrowingId));
		
		if(borrow.getStatus() == BorrowingStatus.RETURNED)
		{
			throw new RuntimeException("Borrowing book is already returned");
		}
		
		Book book = bookRepository.findByIdForUpdate(borrow.getBook().getId())
		              .orElseThrow(() -> new BookNotFoundException("Book is not found with id: "+borrow.getBook().getId()));
		
		book.setAvailableStock(book.getAvailableStock()+1);
		
		borrow.setStatus(BorrowingStatus.RETURNED);
		borrow.setReturnedAt(LocalDateTime.now());
		
		bookRepository.save(book);
		
		Borrowing saveBorrow = borrowRepository.save(borrow);
		
		return convertToBorrowingResponse(saveBorrow);
		
		
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
	
	public DashboardDto getDashboard()
	{
		long totalUsers = userRepository.count();
		long totalBooks = bookRepository.count();
		long totalAvailableCopies = bookRepository.findAll()
				                                  .stream()
				                                  .mapToLong(Book::getAvailableStock)
				                                  .sum();
		long activeBorrowings = borrowRepository.countByStatus(BorrowingStatus.BORROWED);
		long returnedBorrowings = borrowRepository.countByStatus(BorrowingStatus.RETURNED);
		
		DashboardDto dto = new DashboardDto();
		dto.setTotalUsers(totalUsers);
		dto.setTotalBooks(totalBooks);
		dto.setTotalAvailableCopies(totalAvailableCopies);
		dto.setActiveBorrowings(activeBorrowings);
		dto.setReturnedBorrowings(returnedBorrowings);
		
		return dto;
		
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
