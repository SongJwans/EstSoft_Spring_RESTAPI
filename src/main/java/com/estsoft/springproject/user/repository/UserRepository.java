package com.estsoft.springproject.user.repository;

import com.estsoft.springproject.user.domain.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    // email을 가지고 인증하겠습니다.
    // select * from users where email = ${email};
   Optional<Users> findByEmail(String email);
}
