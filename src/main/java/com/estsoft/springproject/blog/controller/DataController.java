package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.dto.ArticleDTO;
import com.estsoft.springproject.blog.domain.dto.CommentDTO;
import com.estsoft.springproject.blog.service.DataImportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {
    private final DataImportService dataImportService;

    @GetMapping("/import-articles")
    public ResponseEntity<List<ArticleDTO>> importArticles() {
        return ResponseEntity.ok(dataImportService.importArticles().stream()
                .map(ArticleDTO::new)
                .toList());
    }

    @GetMapping("/import-comments")
    public ResponseEntity<List<CommentDTO>> importComments() {
        return ResponseEntity.ok(dataImportService.importComments().stream()
                .map(CommentDTO::new)
                .toList());
    }
}
