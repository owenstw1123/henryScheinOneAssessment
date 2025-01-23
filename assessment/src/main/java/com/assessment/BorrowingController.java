package com.assessment;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
class BorrowingController {

    private final BorrowingRepository repository;
    private final BookRepository book_repository;
    private final BorrowerRepository borrower_repository;

    BorrowingController(BorrowingRepository repository, BookRepository book_repository, BorrowerRepository borrower_repository) {
        this.repository = repository;
        this.book_repository = book_repository;
        this.borrower_repository = borrower_repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/borrowings")
    List<Borrowing> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @GetMapping("/borrowings/borrower_id/{borrower_id}")
    List<Borrowing> allBooksByBorrower(@PathVariable Long borrower_id) {
        borrower_repository.findById(borrower_id)
                .orElseThrow(() -> new BorrowerNotFoundException(borrower_id));
        return repository.findByBorrowerIdAndReturnedOnIsNull(borrower_id);
    }

    @GetMapping("/borrowings/borrow/book_id/{book_id}/borrower_id/{borrower_id}")
    Borrowing borrowBook(@PathVariable Long book_id, @PathVariable Long borrower_id) {
        book_repository.findById(book_id)
                .orElseThrow(() -> new BookNotFoundException(book_id));
        borrower_repository.findById(borrower_id)
                .orElseThrow(() -> new BorrowerNotFoundException(borrower_id));
        return (Borrowing) repository.findByBookIdAndReturnedOnIsNull(book_id)
                .map(borrowing -> {
                    throw new BorrowingNotAvailableException(book_id);
                })
                .orElseGet(() -> {
                    return repository.save( new Borrowing(book_id, borrower_id, new Date(), null) );
                });
    }

    @GetMapping("/borrowings/return/book_id/{book_id}/borrower_id/{borrower_id}")
    Borrowing returnBook(@PathVariable Long book_id, @PathVariable Long borrower_id) {
        return repository.findByBookIdAndBorrowerIdAndReturnedOnIsNull(book_id, borrower_id)
                .map(borrowing -> {
                    borrowing.setReturnedOn(new Date());
                    return repository.save(borrowing);
                })
                .orElseThrow(() -> new BorrowingNotFoundException(book_id, borrower_id));
    }
}
