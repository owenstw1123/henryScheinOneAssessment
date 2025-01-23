package com.assessment;

public class BorrowingNotAvailableException extends RuntimeException {
    public BorrowingNotAvailableException(Long book_id) {
        super("Book " + book_id + "is not currently available to check out.");
    }
}
