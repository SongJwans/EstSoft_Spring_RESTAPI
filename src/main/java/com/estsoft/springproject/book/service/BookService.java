package com.estsoft.springproject.book.service;

import com.estsoft.springproject.book.domain.Book;
import com.estsoft.springproject.book.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    // 책 생성
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    // 책 (전체) 조회
    public List<Book> findAll() {
        // Sort.by -> id를 기준으로 정렬
        // Select * from book order by id;
        return bookRepository.findAll(Sort.by("id"));
    }

    public Book findById(String id) {
        // Optional 객첵 때문에 예외시 빈객체 호출
        return bookRepository.findById(id).orElse(new Book());
    }

    public Book saveOne(Book book) {
        return bookRepository.save(book);
    }
}
