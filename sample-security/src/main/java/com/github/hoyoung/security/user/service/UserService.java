package com.github.hoyoung.security.user.service;

import com.github.hoyoung.security.entity.User;
import com.github.hoyoung.security.model.UserDetailsContext;
import com.github.hoyoung.security.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by HoYoung on 2021/02/16.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User loginUser = this.userRepository.findByEmail(username).orElseThrow(() ->
        new UsernameNotFoundException("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다."));

    return UserDetailsContext.withUsername(username)
        .password(loginUser.getPassword())
        .roles(loginUser.getRole())
        .build();
  }
}
