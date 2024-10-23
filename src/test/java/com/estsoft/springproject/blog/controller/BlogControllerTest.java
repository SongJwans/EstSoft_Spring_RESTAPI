package com.estsoft.springproject.blog.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.request.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.request.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@AutoConfigureMockMvc
@SpringBootTest
class BlogControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    // jackson
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BlogRepository repository;

    @Autowired
    BlogService blogService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
    }

    // POST /articles API 테스트
    @DisplayName("블로그 내용 저장")
    @Test
    public void addArticle() throws Exception {
        // given: article 저장
//        Article article = new Article("제목", "내용");
        String url = "/articles";
        String title = "제목";
        String content = "내용";
        AddArticleRequest request = new AddArticleRequest(title, content);

//        repository.save(article);
        // body 값을 직렬화 (Object -> Json)
        String json = objectMapper.writeValueAsString(request);

        // when : POST /articles API 호출
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(json));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.content").value(request.getContent()));

        List<Article> articleList = repository.findAll();
        assertThat(articleList.size()).isEqualTo(1);
    }

    @DisplayName("블로그 내용 조회")
    @Test
    public void testFindAll() throws Exception {
        // given: 조회 API 에 필요한 값
        // Entity 생성
        Article article = new Article("여행 기록 - LA", "Hollywood에서 외치다.");
        Article article2 = new Article("여행 기록 - San Francisco", "pier 39 에서 크램차우더를 먹으면서");

        repository.save(article);
        repository.save(article2);

        // when : 조회 API
        ResultActions resultActions = mockMvc.perform(get("/articles").accept(APPLICATION_JSON));
        // then : API 조회 호출 결과 검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()))
                .andExpect(jsonPath("$[1].title").value(article2.getTitle()))
                .andExpect(jsonPath("$[1].content").value(article2.getContent()));
    }

    // data insert (id=1), GET /article/1
    @DisplayName("블로그 단건 조회")
    @Test
    public void findOne() throws Exception {
        // given: data insert
        Article article = new Article("여행 기록 - LA", "HollyWood 에서 외치다.");
        repository.save(article);
        Long id = article.getId();

        // when : API 호출
        ResultActions resultActions = mockMvc.perform(get("/article/{id}", id).accept(APPLICATION_JSON));

        // then : API 호출 결과 검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value(article.getTitle()))
                .andExpect(jsonPath("$.content").value(article.getContent()));
    }

    @Test
    public void findOneException() throws Exception {
        // When : API 호출
        ResultActions resultActions = mockMvc.perform(get("/article/{id}", 1L).accept(APPLICATION_JSON));

        // then : Exception 검증, resultActions STATUS CODE 검증
        resultActions.andExpect(status().isBadRequest());

        assertThrows(IllegalArgumentException.class, () -> blogService.findById(1L));
    }

    // 블로그 글 삭제 API 호출
    @Test
    public void deleteTest() throws Exception {
        // given: data insert
        Article article = new Article("여행 기록 - LA", "HollyWood 에서 외치다.");

        repository.save(article);

        Long id = article.getId();

        // when : API 호출
        mockMvc.perform(delete("/article/{id}", id).accept(APPLICATION_JSON));
        ResultActions resultActions = mockMvc.perform(get("/articles").accept(APPLICATION_JSON));

        // then : API 호출 결과 검증
        resultActions.andExpect(status().isOk()); // status code 검증
        List<Article> articleList = repository.findAll();
        assertThat(articleList).isEmpty(); // 비어 있는 Data 검증
    }

    // PUT /article/{id}
    @Test
    public void updateArticle() throws Exception {
        // given
        Article article = repository.save(new Article("여행 기록 - LA", "HollyWood 에서 외치다."));
        Long id = article.getId();

        // 수정 데이터 object -> json
        UpdateArticleRequest request = new UpdateArticleRequest("변경 제목", "변경 내용");
        // 직렬화 java -> json writeValueAsString , 역 직렬화 realValue()
        String updateJsonContent = objectMapper.writeValueAsString(request);

        ResultActions resultActions = mockMvc.perform(put("/article/{id}", id)
                .contentType(APPLICATION_JSON)
                .content(updateJsonContent)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(request.getTitle()));
    }

    // 수정 API status code 4xx, 예외 검증 (수정하려는 id 존재하지 않음)
    @Test
    public void updateArticleException() throws Exception {
        // given : id, requestBody
        Long notExistsId = 1000L;
        UpdateArticleRequest request = new UpdateArticleRequest("title", "content");
        // 직렬화
        String requestBody = objectMapper.writeValueAsString(request);

        // when : 수정 API 호출 (/article/{id}, requestBody)
        ResultActions resultActions = mockMvc.perform(put("/article/{id}", notExistsId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then 400 Bad Request, Exception 검증
        resultActions.andExpect(status().isBadRequest());
        assertThrows(IllegalArgumentException.class, () -> blogService.update(notExistsId,request));
    }
}