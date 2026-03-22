package com.polarbookshop.catalog_service.domain;

import static com.polarbookshop.catalog_service.domain.BookFixture.createBook;
import static org.assertj.core.api.Assertions.assertThat;

import com.polarbookshop.catalog_service.config.DataConfig;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
    replace = AutoConfigureTestDatabase.Replace.NONE // 테스트용 임베디드 DB 대신 Testcontainers PostgreSQL 사용
)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate; // 데이터를 넣는 경로와 검증하는 경로를 분리 목적

    @Test
    void findBookByIsbnWhenExisting() {
        var bookIsbn = "1234561237";
        var book = createBook(bookIsbn, 12.90);
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().isbn()).isEqualTo(bookIsbn);
    }
}
