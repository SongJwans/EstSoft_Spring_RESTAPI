package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.request.CommentRequestDTO;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.repository.CommentRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private CommentService commentService;

    private Article article;
    private CommentRequestDTO commentRequestDTO;

    @BeforeEach
    void setUp() {
        // Given article and CommentRequestDTO to be used for testing
        String title = "Test Title";
        String content = "Test Content";

        article = Article.builder().title(title).content(content).build();
        commentRequestDTO = new CommentRequestDTO("Test comment body");
    }

    @Test
    void testSaveComment() {
        // Given
        when(blogRepository.findById(anyLong())).thenReturn(Optional.of(article));
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment("Test comment body", article));

        // When
        Comment savedComment = commentService.save(1L, commentRequestDTO);

        // Then
        assertNotNull(savedComment);
        assertEquals("Test comment body", savedComment.getBody());
        assertEquals(article, savedComment.getArticle());
        verify(commentRepository, times(1)).save(any(Comment.class));  // verify that save was called
    }

    @Test
    void testFindById() {
        // Given
        Comment comment = new Comment("Test comment body", article);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        // When
        Comment foundComment = commentService.findById(1L);

        // Then
        assertNotNull(foundComment);
        assertEquals("Test comment body", foundComment.getBody());
    }

    @Test
    void testFindByIdThrowsExceptionIfCommentNotFound() {
        // Given
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> commentService.findById(1L));
    }

    @Test
    void testUpdateComment() {
        // Given
        Comment existingComment = new Comment("Existing comment body", article);
        CommentRequestDTO updatedRequest = new CommentRequestDTO("Updated comment body");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(new Comment("Updated comment body", article));

        // When
        Comment updatedComment = commentService.update(1L, updatedRequest);

        // Then
        assertNotNull(updatedComment);
        assertEquals("Updated comment body", updatedComment.getBody());
        verify(commentRepository, times(1)).save(any(Comment.class));  // verify that save was called
    }

    @Test
    void testDeleteByCommentId() {
        // Given
        Long commentId = 1L;
        doNothing().when(commentRepository).deleteById(commentId);

        // When
        commentService.deleteByCommentId(commentId);

        // Then
        verify(commentRepository, times(1)).deleteById(commentId);  // verify that deleteById was called
    }

    @Test
    void testSelectCommentsFindByArticleId() {
        // Given
        Comment comment1 = new Comment("Comment 1", article);
        Comment comment2 = new Comment("Comment 2", article);
        when(commentRepository.findByArticleId(anyLong())).thenReturn(List.of(comment1, comment2));

        // When
        List<Comment> comments = commentService.selectCommentsFindByArticleId(1L);

        // Then
        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertEquals("Comment 1", comments.get(0).getBody());
        assertEquals("Comment 2", comments.get(1).getBody());
    }
}
