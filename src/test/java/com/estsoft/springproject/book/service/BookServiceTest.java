package com.estsoft.springproject.book.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.estsoft.springproject.book.domain.Book;
import com.estsoft.springproject.book.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook1;
    private Book testBook2;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {
        // 공통 데이터 초기화
        testBook1 = new Book("testId1", "testName1", "testAuthor1");
        testBook2 = new Book("testId2", "testName2", "testAuthor2");
        bookList = List.of(testBook1, testBook2);
    }

    @Test
    void testSave() {
        // given
        when(bookRepository.save(any(Book.class))).thenReturn(testBook1);

        // when
        Book savedBook = bookService.save(testBook1);

        // then
        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isEqualTo(testBook1.getId());
        verify(bookRepository, times(1)).save(testBook1);
    }

    @Test
    void testFindAll() {
        // given
        when(bookRepository.findAll(Sort.by("id"))).thenReturn(bookList);

        // when
        List<Book> result = bookService.findAll();

        // then
        assertThat(result).hasSize(2).containsExactly(testBook1, testBook2);
        verify(bookRepository, times(1)).findAll(Sort.by("id"));
    }

    @Test
    void testFindById_Found() {
        // given
        String bookId = testBook1.getId();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook1));

        // when
        Book result = bookService.findById(bookId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookId);
        verify(bookRepository, times(1)).findById(bookId);
    }
    @Test
    void testFindById_NotFound() {
        // given
        String bookId = "nonExistentId";
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // when
        Book result = bookService.findById(bookId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull(); // 빈 Book 객체의 기본 값 확인
        verify(bookRepository, times(1)).findById(bookId);
    }
    @Test
    void testSaveOne() {
        // given
        when(bookRepository.save(any(Book.class))).thenReturn(testBook1);

        // when
        Book result = bookService.saveOne(testBook1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testBook1.getId());
        assertThat(result.getName()).isEqualTo(testBook1.getName());
        assertThat(result.getAuthor()).isEqualTo(testBook1.getAuthor());
        verify(bookRepository, times(1)).save(testBook1);
    }
}
