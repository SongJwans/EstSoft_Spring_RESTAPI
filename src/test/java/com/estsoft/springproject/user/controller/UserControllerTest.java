package com.estsoft.springproject.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class UserControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    // 각각의 테스트를 실행하기 전마다 실행
    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testSaveUser() throws Exception {

        // given
        AddUserRequest addUserRequest = new AddUserRequest("test@test.com", "1234");
        String jsonUser = objectMapper.writeValueAsString(addUserRequest);

        // when
        ResultActions resultActions = mvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser));

        // then
        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"));

    }
}