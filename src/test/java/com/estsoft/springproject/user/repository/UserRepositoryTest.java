package com.estsoft.springproject.user.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import com.estsoft.springproject.user.domain.Users;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() {
        // given : when 절에서 조회하려는 사용자 정보 저장
        Users user = getUser();
        userRepository.save(user);

        // when
        Users returnUser = userRepository.findByEmail(user.getEmail()).orElseThrow();

        // then
        assertEquals(returnUser.getEmail(), user.getEmail());
        assertEquals(returnUser.getEmail(), user.getEmail());
    }

    @Test
    public void testSave() {
        // given : when 절에서 조회하려는 사용자 정보 저장
        Users user = getUser();

        // when
        Users saved = userRepository.save(user);

        // then
        assertEquals(saved.getEmail(), user.getEmail());
    }

    // 사용자 전체 조회 기능
    @Test
    public void testFindAll() {
        // given
        Users user1 = getUser("test1@test.com", "pw2134");
        Users user2 = getUser("test2@test.com", "pw1234");
        Users user3 = getUser("test3@test.com", "pw5678");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        // when
        List<Users> all = userRepository.findAll();

        // then
        assertEquals(all.size(), 3);

        assertTrue(all.contains(user1)); // user1이 포함되어야 함
        assertTrue(all.contains(user2));
        assertTrue(all.contains(user3));

    }

    private Users getUser() {
        String email = "test@test.com";
        String password = "pw123456";
        return new Users(email, password);
    }

    private Users getUser(String email, String password) {
        return new Users(email, password);
    }
}