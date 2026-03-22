package com.polarbookshop.catalog_service;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.polarbookshop.catalog_service.domain.BookNotFoundException;
import com.polarbookshop.catalog_service.domain.BookService;
import com.polarbookshop.catalog_service.web.BookController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * [실무] @MockitoBean과 같은 기능을 사용할경우 테스트 간의 Application Context를 공유할 수 없어 전체 테스트 수행시간이 길어질 수 있다
     * <br>
     * @see <a href="https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/ctx-management/caching.html">[Spring Docs] Context Caching</a>
     */
    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "73737313940";
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);
        mockMvc.perform(get("/books/{isbn}", isbn))
            .andExpect(status().isNotFound());
    }
}
