package com.github.hoyoung.security.authentication;

import com.github.hoyoung.security.model.response.AuthErrorResponseEntity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/15.
 * 인증 실패 처리 클래스 [401]
 */
@Slf4j
@Component
public class BasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    AuthErrorResponseEntity.body(request, response, exception);
  }
}
