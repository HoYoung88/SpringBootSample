package com.github.hoyoung.security.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.security.exception.BasicAuthenticationServiceException;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by HoYoung on 2021/02/15.
 */
public class AuthErrorResponseEntity {
  public static void body(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    ApiErrorResponse apiErrorResponse = ApiErrorResponse.unauthorized()
        .message(exception.getMessage())
        .build();

    if(exception instanceof BasicAuthenticationServiceException) {
      BasicAuthenticationServiceException e = (BasicAuthenticationServiceException) exception;
      apiErrorResponse = e.getApiErrorResponse();
    }

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(apiErrorResponse.getStatus().value());

    try (OutputStream os = response.getOutputStream()) {
      new ObjectMapper().writeValue(os, apiErrorResponse);
      os.flush();
    }
  }
}
