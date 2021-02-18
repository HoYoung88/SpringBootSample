package com.github.hoyoung.security.authentication.filter;


import static com.github.hoyoung.web.status.service.BaseServiceStatus.METHOD_NOT_ALLOWED;

import com.github.hoyoung.security.exception.AsyncAuthenticationServiceException;
import com.github.hoyoung.security.handler.BasicAuthenticationFailureHandler;
import com.github.hoyoung.security.handler.BasicAuthenticationSuccessHandler;
import com.github.hoyoung.security.model.dto.UserDto;
import com.github.hoyoung.web.status.service.BaseServiceStatus;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Slf4j
public class BasicAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/processing/login", HttpMethod.POST.name());

  private BasicAuthenticationSuccessHandler authenticationSuccessHandler;
  private BasicAuthenticationFailureHandler authenticationFailureHandler;

  public BasicAuthenticationProcessingFilter() {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
  }

  public BasicAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
  }

  public void setAuthenticationSuccessHandler(
      BasicAuthenticationSuccessHandler authenticationSuccessHandler) {
    this.authenticationSuccessHandler = authenticationSuccessHandler;
  }

  public void setAuthenticationFailureHandler(
      BasicAuthenticationFailureHandler authenticationFailureHandler) {
    this.authenticationFailureHandler = authenticationFailureHandler;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, ServletException {
    log.warn("===== Basic Login Request Authentication =====");

    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String keyName = headerNames.nextElement();
      log.warn("{} :: {}", keyName, request.getHeader(keyName));
    }

    if (!request.getMethod().equals(HttpMethod.POST.name())) {
      throw new AsyncAuthenticationServiceException(METHOD_NOT_ALLOWED,
          METHOD_NOT_ALLOWED.format(request.getMethod()),
          HttpStatus.METHOD_NOT_ALLOWED);
    }

    String contentType = Optional.ofNullable(request.getContentType()).orElse("");

//    if(!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
//      throw new AsyncAuthenticationServiceException(UNSUPPORTED_MEDIA_TYPE,
//          UNSUPPORTED_MEDIA_TYPE.format(contentType),
//          HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//    }

//    try {
      UserDto userDto = new UserDto(request.getParameter("username"),
          request.getParameter("password"));

//      UserDto userDto = new ObjectMapper().readValue(request.getReader(), UserDto.class);

//      if(ObjectUtils.isEmpty(userDto.getEmail()) || ObjectUtils.isEmpty(userDto.getPassword())) {
//        throw new AsyncAuthenticationServiceException(BAD_REQUEST,
//            "이메일 또는 패스워드가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
//      }

      UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

      return this.getAuthenticationManager().authenticate(token);

//    } catch (IOException e) {
//      e.printStackTrace();
//      throw new AsyncAuthenticationServiceException(HTTP_MESSAGE_NOT_READABLE,
//          HTTP_MESSAGE_NOT_READABLE.format(contentType),
//          HttpStatus.BAD_REQUEST);
//    }

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    this.authenticationSuccessHandler.onAuthenticationSuccess(request, response, chain, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
  }
}
