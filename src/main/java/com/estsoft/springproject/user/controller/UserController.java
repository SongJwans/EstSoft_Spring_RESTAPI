package com.estsoft.springproject.user.controller;

import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.estsoft.springproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // POST /user 요청 받고 회원가입 처리 -> /login redirect
    @PostMapping("/user")
    public String save(@RequestBody AddUserRequest request) {
        userService.save(request);
        return "redirect:login";
    }
}
