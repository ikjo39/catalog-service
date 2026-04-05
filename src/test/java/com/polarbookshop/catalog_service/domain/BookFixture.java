package com.polarbookshop.catalog_service.domain;

public class BookFixture {

    public static Book createBook() {
        return Book.of("1234567890", "Title", "Author", 9.90, "BIG FOOT");
    }

    public static Book createBook(String isbn) {
        return Book.of(isbn, "Title", "Author", 9.90, "BIG FOOT");
    }

    public static Book createBook(String isbn, Double price) {
        return Book.of(isbn, "Title", "Author", price, "BIG FOOT");
    }
}
