package com.estsoft.springproject.blog.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.request.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.response.ArticleResponse;
import com.estsoft.springproject.blog.service.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// 단위 테스트를 위한 사전 작업
@ExtendWith(MockitoExtension.class)
class BlogControllerTest2 {

    @InjectMocks
    private BlogController blogController;

    @Mock
    private BlogService blogService;

    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }

    @Test
    public void testArticle() throws Exception {
        // given : API 호출에 필요한 데이터 만들기
        // { "title" : "mock_title",
        //   "content" : "mock_content" }
        // 직렬화 : 객체 -> JSON

        String title = "mock_title";
        String content = "mock_content";

        AddArticleRequest request = new AddArticleRequest(title, content);
        ObjectMapper objectMapper = new ObjectMapper();
        String articleJson = objectMapper.writeValueAsString(request);

        // stub (service.saveArticle 호출 시 dnldptj aksemf
        Mockito.when(blogService.save(any()))
                .thenReturn(new Article(title, content));

        // when : API 호출
        ResultActions resultActions = mockMvc.perform(post("/api/articles")
                .content(articleJson)
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then : 호출 결과 검증
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("content").value(content))
                .andExpect(jsonPath("title").value(title));
    }

    @Test
    public void testDelete() throws Exception {
        // given
        Long id = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/articles/{id}", id));

        // then : HTTP status code 검증, service.deleteBy 호출 횟수 검증
        resultActions.andExpect(status().isOk());

        verify(blogService, times(1)).deleteById(id);
    }

    // GET /articles/{id}
    @Test
    public void testFindOne() throws Exception {
        // given
        Long id = 1L;

        // stubing : article 객체를 만들어준다.
        Mockito.doReturn(new Article("title", "content")).when(blogService).findById(id);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/{id}", id));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("title").value("title"));
    }
}