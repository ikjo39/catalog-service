package com.polarbookshop.catalog_service;

import static com.polarbookshop.catalog_service.domain.BookFixture.createBook;
import static org.assertj.core.api.Assertions.assertThat;

import com.polarbookshop.catalog_service.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        var expectedBook = createBook();

        webTestClient.post()
            .uri("/books")
            .bodyValue(expectedBook)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Book.class).value(actualBook -> {
                assertThat(actualBook).isNotNull();
                assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
            });
    }
}
