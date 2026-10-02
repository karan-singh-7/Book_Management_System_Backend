package com.library.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashboardDto {

	 private long totalUsers;
	 private long totalBooks;
	 private long totalAvailableCopies;
	 private long activeBorrowings;
	 private long returnedBorrowings;
	 
}
