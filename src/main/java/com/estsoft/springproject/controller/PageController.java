package com.estsoft.springproject.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Person   GET /thymeleaf/example
    @GetMapping("/thymeleaf/example")
    public String show(Model model) {
        Person person = new Person();
        person.setId(1L);
        person.setName("송주환");
        person.setAge(26);
        person.setHobbies(Arrays.asList("헬스", "줄넘기", "복싱", "..."));

        model.addAttribute("person", person);
        model.addAttribute("today", LocalDateTime.now());
        return "examplePage"; // html 페이지
    }
}
