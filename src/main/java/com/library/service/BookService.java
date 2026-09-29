package com.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.library.dto.book.BookCreateRequest;
import com.library.dto.book.BookResponse;
import com.library.dto.book.BookUpdateRequest;
import com.library.entity.Book;
import com.library.exception.BookNotFoundException;
import com.library.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	
	public final BookRepository bookRepository;
	
	public BookResponse createBook(BookCreateRequest request)
	{
		Book book = new Book();
		
		 book.setTitle(request.getTitle());
	     book.setAuthor(request.getAuthor());
	     book.setIsbn(request.getIsbn());
	     book.setCategory(request.getCategory());
	     book.setDescription(request.getDescription());
	     
	     // admin provide the quantity of book
	     book.setTotalStock(request.getTotalStock());
	     
	     // initially all the book is available
	     book.setAvailableStock(request.getTotalStock());
	     
	     Book savedBook = bookRepository.save(book);
	     
	     return convertToResponse(savedBook);
	}
	
	public BookResponse getBookById(Long bookId)
	{
		Book book = bookRepository.findById(bookId)
		              .orElseThrow(() ->  new BookNotFoundException("Book not found with id: "+bookId));
		
		return convertToResponse(book);
	}
	
	public List<BookResponse> getAllBooks()
	{
		return bookRepository.findAll()
				             .stream()
				             .map(this::convertToResponse)
				             .toList();
	}
	
	public BookResponse updateBook(Long id,BookUpdateRequest request)
    {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with id: " + id));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setDescription(request.getDescription());

        Book updatedBook = bookRepository.save(book);

        return convertToResponse(updatedBook);
    }

    public void deleteBook(Long id) 
    {

        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found with id: " + id));

        bookRepository.delete(book);
    }
    
    public List<BookResponse> searchBook(String keyword)
    {
    	  return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword)
    			                .stream()
    			                .map(this::convertToResponse)
    			                .toList();
    }
    
    public Page<BookResponse> getBooksWithPagination(int page, int size, String sortBy, String direction)
    {
    	    Sort sort;
    	    if(sortBy.equalsIgnoreCase("desc"))
    	    {
    	    	  sort = Sort.by(sortBy).descending();
    	    }
    	    else
    	    {
    	    	  sort= Sort.by(sortBy).ascending();
    	    }
    	    
    	    Pageable pageable = PageRequest.of(page, size, sort);
    	    
    	    return bookRepository.findAll(pageable)
    	    		                 .map(this::convertToResponse);
    }
    
    public BookResponse updateQuantity(Long id, int quantity)
    {
    	    Book book = bookRepository.findById(id)
    	    		        .orElseThrow(()-> new BookNotFoundException("Book not found with id: "+id)); 
    	    
    	    int newTotalStock = book.getTotalStock()+quantity;
    	    int newAvailableStock = book.getAvailableStock()+quantity;
    	    
    	    if(newTotalStock<0)
    	    {
    	    	   throw new RuntimeException("Total stock cannot be negative");
    	    }
    	    
    	    if(newAvailableStock<0)
    	    {
    	    	   throw new RuntimeException("Available stock cannot be negative");
    	    }
    	    
    	    book.setTotalStock(newTotalStock);
    	    book.setAvailableStock(newAvailableStock);
    	    
    	    return convertToResponse(book);
    }
    
	private BookResponse convertToResponse(Book book) 
    {

	        BookResponse response = new BookResponse();

	        response.setId(book.getId());
	        response.setTitle(book.getTitle());
	        response.setAuthor(book.getAuthor());
	        response.setIsbn(book.getIsbn());
	        response.setCategory(book.getCategory());
	        response.setDescription(book.getDescription());
	        response.setTotalStock(book.getTotalStock());
	        response.setAvailableStock(book.getAvailableStock());

	        return response;
	}
}