package com.estsoft.springproject.blog.domain.dto.response;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse {
    private Long article_id;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public ArticleResponse(Article article) {
        this.article_id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
    }
}
