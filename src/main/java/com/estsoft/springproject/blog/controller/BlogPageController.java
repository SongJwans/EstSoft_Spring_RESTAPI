package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.response.ArticleViewResponse;
import com.estsoft.springproject.blog.service.BlogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BlogPageController {
    private final BlogService blogService;

    @GetMapping("/articles")
    public String showArticle(Model model) {
        List<Article> articleList = blogService.findAll();

        // Article 로 ArticleViewResponse 로 변환해서 보낸다.
        List<ArticleViewResponse> list = articleList.stream()
                .map(ArticleViewResponse::new)
                .toList();

        model.addAttribute("articles", list);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Users users = (Users) authentication.getPrincipal();

        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article"; // article.html
    }

    @GetMapping("/new-article")
    public String addArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) { // 새로운 게시글 생성
            model.addAttribute("article", new ArticleViewResponse());
        } else {// 게시글 수정
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "newArticle";
    }
}
