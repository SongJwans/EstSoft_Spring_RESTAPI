package com.estsoft.springproject.book.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class BookDTO {
    private String id;
    private String name;
    private String author;

    public BookDTO(Book book) {
        this.id = book.id;
        this.name = book.name;
        this.author = book.author;
    }
}
