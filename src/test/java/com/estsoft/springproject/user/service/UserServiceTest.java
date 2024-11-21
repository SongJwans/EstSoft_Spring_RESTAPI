package com.estsoft.springproject.user.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.estsoft.springproject.user.domain.Users;
import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.estsoft.springproject.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository repository;

    @Spy
    BCryptPasswordEncoder encoder;

    @Test
    public void testSave() {
        // given
        String email = "mock_email";
        String password = encoder.encode("mock_password");
        AddUserRequest request = new AddUserRequest(email, password);

        // userRepository.save -> stub
        doReturn(new Users(email, password))
                .when(repository).save(any(Users.class));

        // when : 회원가입 기능 = 사용자 정보 저장
        Users returnSaved = userService.save(request);

        // then
        assertThat(returnSaved.getEmail(), is(email));

        // 메소드가 비지니스 로직에서 몇번 호출되었는지 확인
        verify(repository, times(1)).save(any());
        verify(encoder, times(2)).encode(any());
    }

}