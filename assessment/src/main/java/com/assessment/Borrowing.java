package com.assessment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.Objects;

@Entity
class Borrowing {
    private @Id
    @GeneratedValue Long id;
    private Long bookId;
    private Long borrowerId;
    private Date borrowedOn;
    private Date returnedOn;

    Borrowing() {}

    Borrowing(Long bookId, Long borrowerId) {
        this(bookId, borrowerId, new Date(), null);
    }

    Borrowing(Long bookId, Long borrowerId, Date borrowedOn, Date returnedOn) {
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.borrowedOn = borrowedOn;
        this.returnedOn = returnedOn;
    }

    public Long getId() {
        return this.id;
    }

    public Long getBookId() {
        return this.bookId;
    }

    public Long getBorrowerId() {
        return this.borrowerId;
    }

    public Date getBorrowedOn() {
        return this.borrowedOn;
    }

    public Date getReturnedOn() {
        return this.returnedOn;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setBorrowedOn(Date borrowedOn) {
        this.borrowedOn = borrowedOn;
    }

    public void setReturnedOn(Date returnedOn) {
        this.returnedOn = returnedOn;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Borrowing))
            return false;
        Borrowing borrowing = (Borrowing) o;
        return Objects.equals(this.id, borrowing.id) && Objects.equals(this.bookId, borrowing.bookId)
                && Objects.equals(this.borrowerId, borrowing.borrowerId) && Objects.equals(this.borrowedOn, borrowing.borrowedOn)
                && Objects.equals(this.returnedOn, borrowing.returnedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.bookId);
    }

    @Override
    public String toString() {
        return "Book " + this.bookId + " borrowed by Borrower " + this.borrowerId + "on " + this.borrowedOn;
    }

}
