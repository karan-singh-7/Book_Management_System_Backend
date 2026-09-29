package com.library.controller;

import com.library.dto.book.BookCreateRequest;
import com.library.dto.book.BookResponse;
import com.library.dto.book.BookUpdateRequest;
import com.library.dto.book.StockUpdateRequest;
import com.library.service.BookService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Add new book
    @PostMapping
    public BookResponse createBook(
            @Valid @RequestBody BookCreateRequest request) {

        return bookService.createBook(request);
    }

    // Get all books
    @GetMapping
    public List<BookResponse> getAllBooks() {

        return bookService.getAllBooks();
    }
    
    // search book
    @GetMapping("/search")
    public List<BookResponse> searchBooks(
            @RequestParam String keyword) {

        return bookService.searchBook(keyword);
    }

    // Get book by ID
    @GetMapping("/{id}")
    public BookResponse getBookById(
            @PathVariable Long id) {

        return bookService.getBookById(id);
    }
    
    @GetMapping("/page")
    public Page<BookResponse> getBooksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return bookService.getBooksWithPagination(
                page,
                size,
                sortBy,
                direction
        );
    }

    // Update book details
    @PutMapping("/{id}")
    public BookResponse updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookUpdateRequest request) {

        return bookService.updateBook(id, request);
    }
    
    // update stock    
    @PatchMapping("/{id}/stock")
    public BookResponse updateStock(@PathVariable Long id, @RequestBody StockUpdateRequest request)
    {
    	   return bookService.updateQuantity(id,request.getQuantity());
    }

    // Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}