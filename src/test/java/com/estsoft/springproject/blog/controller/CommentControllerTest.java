package com.estsoft.springproject.blog.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.request.CommentRequestDTO;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private BlogService blogService;

    @Mock
    private CommentService commentService;


    @InjectMocks
    private CommentController commentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Comment comment;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        Article article = new Article("Test Article", "Test Content");

        String commentTitle = "Test Comment Title";
        comment = Comment.builder().body(commentTitle).article(article).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateComment() throws Exception {
        // given
        when(commentService.save(anyLong(), any(CommentRequestDTO.class))).thenReturn(comment);

        // when & then
        mockMvc.perform(post("/api/articles/{articleId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(comment.getBody()));

    }

    @Test
    void testCommentFindById() throws Exception {
        // given
        when(commentService.findById(anyLong())).thenReturn(comment);

        // when & then
        mockMvc.perform(get("/api/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(comment.getBody()));

    }

    @Test
    void testUpdateCommentById() throws Exception {
        // given
        Article article = new Article("test - title", "test - Content");
        CommentRequestDTO request = new CommentRequestDTO("Updated comment");
        Comment updatedComment = new Comment("Updated comment", article);

        when(commentService.update(anyLong(), any(CommentRequestDTO.class))).thenReturn(updatedComment);

        // when & then
        mockMvc.perform(put("/api/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(updatedComment.getBody()));
    }

    @Test
    void testDeleteCommentById() throws Exception {
        // given
        Long commentId = 1L;

        doNothing().when(commentService).deleteByCommentId(commentId);

        // when & then
        mockMvc.perform(delete("/api/comments/{commentId}", commentId))
                .andExpect(status().isOk());

    }

    @Test
    void testFindAllComments() throws Exception {
        // given
        Long articleId = 1L;
        Article article = new Article("test - title", "test - Content");
        Comment comment1 = new Comment("Comment 1", article);
        Comment comment2 = new Comment("Comment 2", article);

        when(blogService.findById(anyLong())).thenReturn(article);
        when(commentService.selectCommentsFindByArticleId(anyLong()))
                .thenReturn(List.of(comment1, comment2));

        // when & then
        mockMvc.perform(get("/api/articles/{articleId}/comments", articleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments[0].body").value(comment1.getBody()))
                .andExpect(jsonPath("$.comments[1].body").value(comment2.getBody()));

    }
}
