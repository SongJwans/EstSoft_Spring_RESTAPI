package com.estsoft.springproject.user.service;

import com.estsoft.springproject.user.domain.Users;
import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.estsoft.springproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    public Users save(AddUserRequest dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        String encodedPassword = encoder.encode(password);

        Users users = new Users(email, encodedPassword);
        return repository.save(users);
    }
}
