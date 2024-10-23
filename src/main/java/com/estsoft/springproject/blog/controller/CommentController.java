package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.request.CommentRequestDTO;
import com.estsoft.springproject.blog.domain.dto.response.ArticleCommentResponse;
import com.estsoft.springproject.blog.domain.dto.response.CommentResponse;
import com.estsoft.springproject.blog.domain.dto.response.CommentResponseDTO;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController// HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {
    private final BlogService service;
    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long articleId,
                                                            @RequestBody CommentRequestDTO request) {
        Comment addComment = commentService.save(articleId, request);
        return ResponseEntity.ok(new CommentResponseDTO(addComment));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> commentFindById(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        return ResponseEntity.ok(new CommentResponseDTO(comment));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateCommentById(@PathVariable Long commentId,
                                                                @RequestBody CommentRequestDTO request) {
        Comment comment = commentService.update(commentId, request);
        return ResponseEntity.ok(new CommentResponseDTO(comment));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> DeleteCommentById(@PathVariable Long commentId) {
        commentService.deleteByCommentId(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<ArticleCommentResponse> findAllComment(@PathVariable Long articleId) {
        Article article = service.findById(articleId);
        List<CommentResponse> list = commentService.selectCommentsFindByArticleId(articleId).stream()
                .map(CommentResponse::new)
                .toList();
        return ResponseEntity.ok(new ArticleCommentResponse(article, list));
    }

}
