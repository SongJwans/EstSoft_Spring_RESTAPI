package com.estsoft.springproject.user.domain.dto;

import lombok.Getter;
import lombok.Setter;

// 회원가입 시 입력 받는 정보
@Getter
@Setter
public class AddUserRequest {
    private String email;
    private String password;
}
