package com.estsoft.springproject.book.controller;

import com.estsoft.springproject.book.domain.Book;
import com.estsoft.springproject.book.domain.BookDTO;
import com.estsoft.springproject.book.service.BookService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private BookService bookService;

    @BeforeEach
    void mockMvcSetUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();

    }

    @Test
    void testSaveBook() throws Exception {
        // given
        String id = "testId";
        String name = "testName";
        String author = "testAuthor";

        // when
        ResultActions resultActions = mvc.perform(post("/books")
                .param("id", id)
                .param("name", name)
                .param("author", author)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

    }

    @Test
    void testShowAll() throws Exception {

        // given
        Book book = new Book("testId", "testName", "testAuthor");
        List<Book> bookList = List.of(book);
        BookDTO bookDTO = new BookDTO(book);
        List<BookDTO> bookDTOList = List.of(bookDTO);

        when(bookService.findAll()).thenReturn(bookList);

        // when
        ResultActions resultActions = mvc.perform(get("/books")
                .accept(MediaType.APPLICATION_JSON));

        // then:    response model, view 검증
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bookManagement"))
                .andExpect(model().attributeExists("bookList"))
                .andExpect(model().attribute("bookList", bookDTOList));
    }

    @Test
    void testShowOne() throws Exception {
        // given
        String bookId = "testId";
        Book book = new Book(bookId, "testName", "testAuthor");
        BookDTO bookDTO = new BookDTO(book);

        // Mocking service behavior
        when(bookService.findById(bookId)).thenReturn(book);

        // when
        ResultActions resultActions = mvc.perform(get("/books/{id}", bookId)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(view().name("bookDetail"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", bookDTO));

        // Verify service call
        verify(bookService).findById(bookId);
    }
}