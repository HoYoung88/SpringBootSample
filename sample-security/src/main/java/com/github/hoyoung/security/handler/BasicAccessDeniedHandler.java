package com.github.hoyoung.security.handler;

import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.security.exception.BasicAuthenticationServiceException;
import com.github.hoyoung.security.model.response.AuthErrorResponseEntity;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/09.
 */
@Slf4j
@Component
public class BasicAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    AuthErrorResponseEntity.body(request, response, new BasicAuthenticationServiceException(
        ApiErrorResponse.forbidden().build()));
  }
}
