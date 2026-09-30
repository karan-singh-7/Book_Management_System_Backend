package com.library.dto.borrowing;

import jakarta.validation.constraints.NotNull;

public class BorrowBookRequest {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}