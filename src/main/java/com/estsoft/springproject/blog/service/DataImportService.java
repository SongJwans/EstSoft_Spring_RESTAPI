package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.ArticleDTO;
import com.estsoft.springproject.blog.domain.dto.CommentDTO;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.repository.CommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class DataImportService {
    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;
    private final RestTemplate restTemplate;

    private static final String ARTICLE_API_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final String COMMENT_API_URL = "https://jsonplaceholder.typicode.com/comments";

    // Article 데이터를 가져와 저장하는 메서드
    public List<Article> importArticles() {
        // json -> List<Article>
        ResponseEntity<List<ArticleDTO>> articlesList = restTemplate.exchange(ARTICLE_API_URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        // ArticleList -> AddArticleRequest
        List<ArticleDTO> addArticleRequests = articlesList.getBody().stream()
                .map(article -> new ArticleDTO(article.getId(), article.getTitle(), article.getBody()))
                .toList();

        // AddArticleRequest -> Article
        List<Article> articles = addArticleRequests.stream()
                .map(articleDTO -> new Article(articleDTO.getTitle(), articleDTO.getBody()))
                .toList();
        return blogRepository.saveAll(articles);
    }

    // Comment 데이터를 가져와 저장하는 메소드
    public List<Comment> importComments() {
        ResponseEntity<List<CommentDTO>> commentList = restTemplate.exchange(COMMENT_API_URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        List<CommentDTO> commentDTOS = commentList.getBody().stream()
                .map(comment -> new CommentDTO(comment.getPostId(), comment.getId(), comment.getBody()))
                .toList();

        List<Comment> comments = commentDTOS.stream().map(comment -> new Comment(comment.getBody(),
                        blogRepository.findById(comment.getPostId())
                                .orElseThrow(() -> new RuntimeException("Article not Find"))))
                .toList();
        return commentRepository.saveAll(comments);
    }

}
