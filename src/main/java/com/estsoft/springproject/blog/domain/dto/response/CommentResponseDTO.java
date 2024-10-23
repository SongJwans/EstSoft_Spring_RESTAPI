package com.estsoft.springproject.blog.domain.dto.response;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentResponseDTO {
    private final Long commentId;
    private final Long articleId;
    private final String body;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    private final ArticleResponse article;

    public CommentResponseDTO(Comment comment) {
        Article articleFromComment = comment.getArticle();

        this.commentId = comment.getId();
        this.articleId = articleFromComment.getId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt();
        this.article = new ArticleResponse(articleFromComment);
    }
}
