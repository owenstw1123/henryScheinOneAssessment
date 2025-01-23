package com.assessment;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Book {
    private @Id
    @GeneratedValue Long id;
    private String title;
    private String author;

    Book() {}

    Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRole(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Book))
            return false;
        Book book = (Book) o;
        return Objects.equals(this.id, book.id) && Objects.equals(this.title, book.title)
                && Objects.equals(this.author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.author);
    }

    @Override
    public String toString() {
        return "Book (" + this.id + "): " + this.title + " by " + this.author;
    }

}
