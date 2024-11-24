package com.estsoft.springproject.blog.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.request.AddArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.service.BlogService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class BlogPageControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mvc;

    @Autowired
    private BlogService blogService;

    @Autowired
    BlogRepository blogRepository;

    // Test Data Setup for Articles
    private AddArticleRequest article1;
    private AddArticleRequest article2;

    @BeforeEach
    public void setUp() {
        blogRepository.deleteAll();

        article1 = new AddArticleRequest("test1", "Los Angeles");
        article2 = new AddArticleRequest("test2", "San Francisco");

        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testShowArticle() throws Exception {
        // given
        List<AddArticleRequest> articles = Arrays.asList(article1, article2);
        blogService.saveAll(articles);

        // when
        ResultActions resultActions = mvc.perform(get("/articles")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(model().attributeExists("articles"))
                .andExpect(view().name("articleList"))
                .andExpect(model().attribute("articles", hasSize(2)))
                .andExpect(model().attribute("articles", hasItem(
                        allOf(
                                hasProperty("title", is("test1")),
                                hasProperty("content", is("Los Angeles"))
                        )
                )))
                .andExpect(model().attribute("articles", hasItem(
                        allOf(
                                hasProperty("title", is("test2")),
                                hasProperty("content", is("San Francisco"))
                        )
                )));
    }

    @Test
    void testShowDetails() throws Exception {

        // given
        Article savedArticle = blogService.save(article1);

        // when
        ResultActions resultActions = mvc.perform(get("/articles/{id}", savedArticle.getId()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("article", hasProperty("title", is("test1"))))
                .andExpect(model().attribute("article", hasProperty("content", is("Los Angeles"))));

    }

    // 새로운 articles 추가
    @Test
    void testNewArticlePage() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/new-article"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(view().name("newArticle"));
    }

    // 기존 글 수정
    @Test
    void testEditArticlePage() throws Exception {
        // given
        Article article = blogService.save(article1); // 실제로 데이터를 저장
        Long articleId = article.getId(); // 저장된 article의 id 가져오기

        // when
        ResultActions resultActions = mvc.perform(get("/new-article?id=" + articleId)
                .contentType(MediaType.TEXT_HTML));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("article", hasProperty("title", is("test1"))))
                .andExpect(model().attribute("article", hasProperty("content", is("Los Angeles"))))
                .andExpect(view().name("newArticle"));
    }

}