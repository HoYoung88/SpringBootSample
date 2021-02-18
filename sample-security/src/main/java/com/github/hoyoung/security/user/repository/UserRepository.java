package com.github.hoyoung.security.user.repository;

import com.github.hoyoung.security.entity.LoginUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HoYoung on 2021/02/16.
 */
public interface UserRepository extends JpaRepository<LoginUser, Long> {
  Optional<LoginUser> findByUsername(String username);
}
