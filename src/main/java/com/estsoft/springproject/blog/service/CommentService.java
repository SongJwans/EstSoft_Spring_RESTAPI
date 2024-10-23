package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.request.CommentRequestDTO;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository repository;


    public Comment save(Long articleId, CommentRequestDTO request) {
        Article article = repository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

//        Comment comment = commentRepository.save(new Comment(request.getBody(), article));
        return commentRepository.save(new Comment(request.getBody(), article));
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public Comment update(Long id, CommentRequestDTO request) {
        Comment comment = findById(id);
        comment.update(request.getBody());
        return commentRepository.save(comment);
    }

    public void deleteByCommentId(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> selectCommentsFindByArticleId(Long articleId){
        return commentRepository.findByArticleId(articleId);
    }
}
