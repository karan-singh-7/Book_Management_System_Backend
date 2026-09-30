package com.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.Enum.BorrowingStatus;
import com.library.entity.Borrowing;

import jakarta.persistence.LockModeType;

public interface BorrowRepository extends JpaRepository<Borrowing, Long> {
	
	boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, BorrowingStatus status);
	
	List<Borrowing> findByUserId(Long userId);
	
	List<Borrowing> findByUserIdAndStatus(Long userId, BorrowingStatus status);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT b FROM borrowings b WHERE b.id = :id")
	Optional<Borrowing> findByIdForUpdate(@Param("id") Long id);
	

}
