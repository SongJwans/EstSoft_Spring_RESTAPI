package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.request.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.request.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    // blog 게시글 저장 (Create)
    public Article save(AddArticleRequest request) {
        return blogRepository.save(request.toEntity());
    }

    public List<Article> saveAll(List<AddArticleRequest> requests) {
        return requests.stream()
                .map(addArticleRequest -> blogRepository.save(addArticleRequest.toEntity()))
                .toList();
    }


    // blog 게시글 조회 (Read)
    public List<Article> findAll() { // 전체 조회
        return blogRepository.findAll();
    }

    public Article findById(Long id) {
        // Optional.get 방법 ->
//        return blogRepository.findById(id).orElse(new Article());
//        return blogRepository.findById(id).orElseGet(Article::new);
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + id));
    }

    // blog 게시글 삭제 (Delete)
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(Long id, UpdateArticleRequest request) {
        Article article = findById(id);
        article.update(request.getTitle(), request.getContent());
        return article;
    }
}