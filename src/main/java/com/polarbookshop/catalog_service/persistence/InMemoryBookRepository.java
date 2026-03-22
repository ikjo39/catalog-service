package com.polarbookshop.catalog_service.persistence;

import com.polarbookshop.catalog_service.domain.Book;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated(since = "JDBC 라이브러리 사용")
public class InMemoryBookRepository {

    private static final Map<String, Book> books = new ConcurrentHashMap<>();

    public Iterable<Book> findAll() {
        return books.values();
    }

    public Optional<Book> findByIsbn(String isbn) {
        return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) : Optional.empty();
    }

    public boolean existsByIsbn(String isbn) {
        return books.get(isbn) != null;
    }

    public Book save(Book book) {
        books.put(book.isbn(), book);
        return book;
    }

    public void deleteByIsbn(String isbn) {
        books.remove(isbn);
    }
}
