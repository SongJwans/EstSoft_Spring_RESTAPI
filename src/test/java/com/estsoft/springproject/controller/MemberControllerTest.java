package com.estsoft.springproject.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class MemberControllerTest {
    // test를 하기 위한 설정
    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper; // JSON 직렬화를 위한 ObjectMapper

    @Autowired
    MemberRepository repository;

    // 각각의 테스트를 실행하기 전마다 실행
    @BeforeEach
    public void setUp() {
        repository.deleteAll();  // 테스트 전에 모든 데이터 삭제
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testGetAllMember() throws Exception {
        // given : 멤버 목록 저장
        Member jh = new Member("주환");
        Member my = new Member("민영");
        repository.save(jh);
        repository.save(my);

        // when : /members 호출
        // mock = Spring MVC 클라이언트의 요청을 받고 호출 응답, DispatcherServlet
        ResultActions resultActions = mvc.perform(get("/members")
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].name").value("주환"))
                .andExpect(jsonPath("$[1].name").value("민영"));
    }

    @Test
    void testSaveMember() throws Exception {
        // given
        Member member = new Member("민환");
        // 직렬화 writeValueAsString
        String memberJson = objectMapper.writeValueAsString(member);

        // when
        ResultActions resultActions = mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(memberJson));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty()) // id가 null이 아님을 확인
                .andExpect(jsonPath("$.name").value("민환"));

        // 데이터베이스 확인
        List<Member> savedMembers = repository.findAll();
        assertThat(savedMembers).hasSize(1);
        assertThat(savedMembers.get(0).getName()).isEqualTo("민환");
        assertThat(savedMembers.get(0).getId()).isNotNull(); // id가 null이 아닌지 확인

    }

    //
    @Test
    void testGetAllMembersWhenEmpty() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/members")
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(0));
    }
}