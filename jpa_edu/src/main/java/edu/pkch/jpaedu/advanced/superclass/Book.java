package edu.pkch.jpaedu.advanced.superclass;

import javax.persistence.*;

@Entity
@DiscriminatorValue("BOOK")
@PrimaryKeyJoinColumn(name = "BOOK_ID")
public class Book extends Item {
    private String author;
    private String isbn;

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}
