package com.estsoft.springproject.user.service;

import com.estsoft.springproject.user.domain.Users;
import com.estsoft.springproject.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailService userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_Success() {
        // given
        String email = "test@test.com";
        Users mockUser = new Users(email, "1234");
        given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));

        // when
        UserDetails userDetails = userDetailService.loadUserByUsername(email);

        // then
        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // given
        String email = "notfound@test.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            userDetailService.loadUserByUsername(email);
        });
        assertEquals(email, exception.getMessage());
        verify(userRepository).findByEmail(email);
    }
}
