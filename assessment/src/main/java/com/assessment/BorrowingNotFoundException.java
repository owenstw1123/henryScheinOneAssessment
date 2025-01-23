package com.assessment;

public class BorrowingNotFoundException extends RuntimeException {
    public BorrowingNotFoundException(Long book_id, Long borrower_id) {
        super("Could not find borrowing with book_id " + book_id + "and borrower_id " + borrower_id);
    }
}
