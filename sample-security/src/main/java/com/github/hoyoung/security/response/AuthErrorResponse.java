package com.github.hoyoung.security.response;

import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.security.exception.AsyncAuthenticationServiceException;
import com.github.hoyoung.web.status.service.BaseServiceStatus;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by HoYoung on 2021/02/15.
 */
public class AuthErrorResponse {
  private final HttpServletRequest request;
  private final HttpServletResponse response;
  private final AuthenticationException exception;
  private static final Locale DEFAULT_LOCALE = Locale.KOREA;
  private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.toString();
  private final ApiErrorResponse apiErrorResponse;

  private AuthErrorResponse(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) {
    this.request = request;
    this.response = response;
    this.exception = exception;
    this.apiErrorResponse = ApiErrorResponse.builder(new HttpHeaders(), HttpStatus.UNAUTHORIZED,
        BaseServiceStatus.UNAUTHORIZED).message(exception.getMessage())
        .build();
  }

  private AuthErrorResponse(HttpServletRequest request, HttpServletResponse response,
      AsyncAuthenticationServiceException exception) {
    this.request = request;
    this.response = response;
    this.exception = exception;
    this.apiErrorResponse = ApiErrorResponse.builder(exception.getStatus(),
        exception.getServiceStatus())
        .message(exception.getMessage())
        .build();
  }

  private void write() throws IOException, ServletException {
//    response.setLocale(DEFAULT_LOCALE);
//    response.setCharacterEncoding(DEFAULT_ENCODING);
//    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//    response.setStatus(apiErrorResponse.getStatus().value());
//
//    try (OutputStream os = response.getOutputStream()) {
//      new ObjectMapper().writeValue(os, apiErrorResponse);
//      os.flush();
//    }

    response.sendRedirect("/login");
  }

  public static void of(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    if(exception instanceof AsyncAuthenticationServiceException) {
      AsyncAuthenticationServiceException e = (AsyncAuthenticationServiceException) exception;
      new AuthErrorResponse(request, response, e).write();
    }else {
      new AuthErrorResponse(request, response, exception).write();
    }
  }

}
