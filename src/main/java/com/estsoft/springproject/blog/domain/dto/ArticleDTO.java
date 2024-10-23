package com.estsoft.springproject.blog.domain.dto;

import com.estsoft.springproject.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private String title;
    private String body;

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.body = article.getContent();
    }
}
