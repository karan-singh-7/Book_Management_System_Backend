package com.library.dto.borrowing;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BorrowingBookResponse {

    private Long id;

    private Long bookId;
    private String bookTitle;

    private Long userId;
    private String userName;

    private LocalDateTime borrowedAt;
    private LocalDateTime returnedAt;

    private String status;
}