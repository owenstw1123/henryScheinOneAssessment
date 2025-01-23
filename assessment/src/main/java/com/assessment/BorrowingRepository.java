package com.assessment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    Optional<Borrowing> findByBookIdAndReturnedOnIsNull(Long book_id);
    Optional<Borrowing> findByBookIdAndBorrowerIdAndReturnedOnIsNull(Long book_id, Long borrower_id);
    List<Borrowing> findByBorrowerIdAndReturnedOnIsNull(Long borrower_id);

}