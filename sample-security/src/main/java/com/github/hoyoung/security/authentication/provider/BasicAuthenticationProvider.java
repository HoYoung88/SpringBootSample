package com.github.hoyoung.security.authentication.provider;

import com.github.hoyoung.security.model.UserDetailsContext;
import com.github.hoyoung.security.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/15.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicAuthenticationProvider implements AuthenticationProvider {

  private final BCryptPasswordEncoder encoder;
  private final UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.warn("===== BasicAuthenticationProvider =====");

    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();

    //DB에 유저 정보 조회
    UserDetailsContext userDetailsContext = (UserDetailsContext) this.userService.loadUserByUsername(username);

    //패스워드 일치 여부
    if (!encoder.matches(password, userDetailsContext.getPassword())) {
      throw new BadCredentialsException("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.");
    }

    return new UsernamePasswordAuthenticationToken(userDetailsContext, null, userDetailsContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
  }
}
