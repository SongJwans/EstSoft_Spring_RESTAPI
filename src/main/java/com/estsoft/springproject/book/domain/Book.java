package com.estsoft.springproject.book.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    String id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String author;
}
