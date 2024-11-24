package com.estsoft.springproject.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class PageControllerTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mvc;

    // 각각의 테스트를 실행하기 전마다 실행
    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testShow() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/thymeleaf/example"));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(model().attributeExists("person"))
                .andExpect(model().attributeExists("today"))
                .andExpect(view().name("examplePage"));         // 뷰 이름이 "examplePage"인지 확인

    }

}