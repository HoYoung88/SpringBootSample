package com.github.hoyoung.security.handler;

import com.github.hoyoung.security.response.AuthErrorResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Component
@Slf4j
public class BasicAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.warn("onAuthenticationFailure :: {}", exception.getMessage());
    AuthErrorResponse.of(request, response, exception);
  }
}
