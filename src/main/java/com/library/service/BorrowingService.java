package com.library.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.Enum.BorrowingStatus;
import com.library.dto.borrowing.BorrowBookRequest;
import com.library.dto.borrowing.BorrowingBookResponse;
import com.library.entity.Book;
import com.library.entity.Borrowing;
import com.library.entity.User;
import com.library.repository.BookRepository;
import com.library.repository.BorrowRepository;
import com.library.repository.UserRepository;

@Service
public class BorrowingService {

	private final BorrowRepository borrowRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	
	public BorrowingService(BorrowRepository borrowRepository, BookRepository bookRepository,
			UserRepository userRepository) {
		super();
		this.borrowRepository = borrowRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}
	
	@Transactional
	public BorrowingBookResponse borrowBook(Long userId, BorrowBookRequest request)
	{
		//1. find user
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		
		//2. find book
		Book book = bookRepository.findByIdForUpdate(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found"));
		
		//3. check available stock
		if(book.getAvailableStock()<=0)
		{
			throw new RuntimeException("Book is currently unavailable");
		}
		
		//4. check for duplicate borrowing
		boolean alreadyBorrowed = borrowRepository.existsByUserIdAndBookIdAndStatus(userId, book.getId(), BorrowingStatus.BORROWED);
		
		if(alreadyBorrowed)
		{
			throw new RuntimeException("You have already borrowed this book");
		}
		
		//5. Decrease available stock
		book.setAvailableStock(book.getAvailableStock()-1);
		
		bookRepository.save(book);
		
		//6. create borrowing record
		Borrowing borrow = new Borrowing();
		borrow.setUser(user);
		borrow.setBook(book);
		borrow.setBorrowedAt(LocalDateTime.now());
		borrow.setStatus(BorrowingStatus.BORROWED);
		
		//7. save borrowing record
		Borrowing savedBorrow = borrowRepository.save(borrow);
		
		//8. convert to borrowResponse and return 
		return convertToResponse(savedBorrow);
		
	}
	
	@Transactional
	public BorrowingBookResponse returnBook(Long bookId, String email)
	{
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		Borrowing borrowing = borrowRepository.findByIdForUpdate(bookId).orElseThrow(() -> new RuntimeException("Borrowing record not found"));
		
		if(!borrowing.getUser().getId().equals(user.getId()))
		{
			throw new RuntimeException("You cannot return another use book");
		}
		
		// make sure it has not bean already returned
		if(borrowing.getStatus() == BorrowingStatus.RETURNED)
		{
			throw new RuntimeException("Book has already bean returned");
		}
		
		// lock the book row before changing stock
		
		Book book = bookRepository.findByIdForUpdate(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		
		// increase the available book
		book.setAvailableStock(book.getAvailableStock()+1);
		
		//update borrowing
		borrowing.setStatus(BorrowingStatus.RETURNED);
		borrowing.setReturnedAt(LocalDateTime.now());
		
		bookRepository.save(book);
		
		Borrowing savedBorrowing = borrowRepository.save(borrowing);
		
		return convertToResponse(savedBorrowing);
	}
	
	public List<BorrowingBookResponse> getMyBorrowingHistory(String email)
	{
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		List<Borrowing> borrowings = borrowRepository.findByUserId(user.getId());
		
		return borrowings.stream()
				         .map(this::convertToResponse)
				         .toList();
	}
	
	public List<BorrowingBookResponse> getMyActiveBorrowings(String email)
	{
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
		
		List<Borrowing> activeBorrowings = borrowRepository.findByUserIdAndStatus(user.getId(), BorrowingStatus.BORROWED);
		
		return activeBorrowings.stream()
				               .map(this::convertToResponse)
				               .toList();
	}
	
	private BorrowingBookResponse convertToResponse(
            Borrowing borrowing) {

        BorrowingBookResponse response =
                new BorrowingBookResponse();

        response.setId(borrowing.getBorrowId());

        response.setUserId(
                borrowing.getUser().getId());

        response.setUserName(
                borrowing.getUser().getName());

        response.setBookId(
                borrowing.getBook().getId());

        response.setBookTitle(
                borrowing.getBook().getTitle());

        response.setBorrowedAt(
                borrowing.getBorrowedAt());

        response.setReturnedAt(
                borrowing.getReturnedAt());

        response.setStatus(
                borrowing.getStatus().name());

        return response;
	}
	
}
