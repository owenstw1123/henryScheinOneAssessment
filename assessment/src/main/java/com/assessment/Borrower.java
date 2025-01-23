package com.assessment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
class Borrower {
    private @Id
    @GeneratedValue Long id;
    private String name;

    Borrower() {}

    Borrower(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String title) {
        this.name = title;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;
        if (!(o instanceof Borrower))
            return false;
        Borrower borrower = (Borrower) o;
        return Objects.equals(this.id, borrower.id) && Objects.equals(this.name, borrower.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    @Override
    public String toString() {
        return "Borrower (" + this.id + "): " + this.name;
    }

}
