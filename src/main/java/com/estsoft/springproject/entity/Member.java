package com.estsoft.springproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;        // DB 테이블의 id와 컬럼 매칭

    @Column(name = "name", nullable = false)
    private String name;    // DB 테이블의 name과 컬럼 매칭

    public Member(String name) {
        this.name = name;
    }
}