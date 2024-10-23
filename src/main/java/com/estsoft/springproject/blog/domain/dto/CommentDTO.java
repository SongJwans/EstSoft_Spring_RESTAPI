package com.estsoft.springproject.blog.domain.dto;

import com.estsoft.springproject.blog.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long postId;
    private Long id;
    private String body;

    public CommentDTO(Comment comment) {
        this.postId = comment.getArticle().getId();
        this.id = comment.getId();
        this.body = comment.getBody();
    }
}
