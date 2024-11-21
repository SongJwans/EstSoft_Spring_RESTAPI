package com.estsoft.springproject.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 회원가입 시 입력 받는 정보
@Getter
@Setter
@AllArgsConstructor
public class AddUserRequest {
    private String email;
    private String password;
}
