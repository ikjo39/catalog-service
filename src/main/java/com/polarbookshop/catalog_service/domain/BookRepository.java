package com.polarbookshop.catalog_service.domain;

import java.util.Optional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    @Transactional
    @Modifying // 데이터베이스의 상태를 수정할 연산임을 나타냄
    @Query("DELETE FROM Book WHERE isbn = :isbn")
    void deleteByIsbn(String isbn);
}
