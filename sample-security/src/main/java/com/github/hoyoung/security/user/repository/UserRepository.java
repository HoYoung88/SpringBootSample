package com.github.hoyoung.security.user.repository;

import com.github.hoyoung.security.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HoYoung on 2021/02/16.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
}
