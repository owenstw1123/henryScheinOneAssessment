package com.assessment;

public class BorrowerNotFoundException extends RuntimeException {
    public BorrowerNotFoundException(Long id) {
        super("Could not find borrower " + id);
    }
}
