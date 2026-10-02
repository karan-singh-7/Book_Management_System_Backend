package com.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.entity.Book;

import jakarta.persistence.LockModeType;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title,
            String author
    );
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
	 @Query("SELECT b FROM Book b WHERE b.id = :id")
	 Optional<Book> findByIdForUpdate(@Param("id") Long id);
}
