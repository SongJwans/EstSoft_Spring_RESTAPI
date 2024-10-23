package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.request.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.response.ArticleResponse;
import com.estsoft.springproject.blog.domain.dto.request.UpdateArticleRequest;
import com.estsoft.springproject.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController// HTTP Response Body에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "블로그 CRUD")
public class BlogController {
    private final BlogService service;

    // HTTP요청이 'POST /api/articles' 경로일 때 해당 메소드로 매핑
    @PostMapping("/articles")
    @Operation(summary = "블로그 전체 목록 보기", description = "블로그 메인 화면에서 보여주는 전체 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "100", description = "요청에 성공했습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<ArticleResponse> addArticle(
            @RequestBody AddArticleRequest request) { // RequestBody로 요청 본문 값 매핑
        // logging
        // trace, debug, info, warn, error
        log.debug(request.getTitle(), request.getContent());
        log.info("request: {}, {}", request.getTitle(), request.getContent());
        Article article = service.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ArticleResponse(article));
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> list = service.findAll().stream()
                .map(ArticleResponse::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/articles/{id}")
    @Parameter(name = "id", description = "블로그 글 ID", example = "45")
    public ResponseEntity<ArticleResponse> findById(@PathVariable Long id) {
        Article article = service.findById(id); // id가 없으면 5XX error 이걸 4XX 에러로 바꿔야 한다.
        // Article -> ArticleResponse
        return ResponseEntity.ok(new ArticleResponse(article));
    }

    // DELETE /article/{id}
    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> updateById(@PathVariable Long id,
                                                      @RequestBody UpdateArticleRequest request) {
        Article updateArticle = service.update(id, request);
        return ResponseEntity.ok(new ArticleResponse(updateArticle));
    }




    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}