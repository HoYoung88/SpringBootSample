package com.github.hoyoung.security.authentication.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.security.exception.BasicAuthenticationServiceException;
import com.github.hoyoung.security.handler.BasicAuthenticationResultHandler;
import com.github.hoyoung.security.model.dto.UserDto;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Slf4j
public class BasicAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

  public static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher("/authorization", HttpMethod.POST.name());

  private BasicAuthenticationResultHandler basicAuthenticationResultHandler;

  public BasicAuthenticationProcessingFilter() {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
  }

  public BasicAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
    super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
  }

  public void setBasicAuthenticationResultHandler(
      BasicAuthenticationResultHandler basicAuthenticationResultHandler) {
    this.basicAuthenticationResultHandler = basicAuthenticationResultHandler;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, ServletException {
    log.warn("===== Basic Login Request Authentication =====");

    if (!request.getMethod().equals(HttpMethod.POST.name())) {
      throw new BasicAuthenticationServiceException(ApiErrorResponse.methodNotAllowed()
          .method(request.getMethod())
          .build());
    }

    String contentType = Optional.ofNullable(request.getContentType()).orElse("");

    if(!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
      throw new BasicAuthenticationServiceException(ApiErrorResponse.unsupportedMediaType()
          .contentType(contentType)
          .build());
    }

    try {

      UserDto userDto = new ObjectMapper().readValue(request.getReader(), UserDto.class);

      if(ObjectUtils.isEmpty(userDto.getEmail()) || ObjectUtils.isEmpty(userDto.getPassword())) {
        throw new BasicAuthenticationServiceException(ApiErrorResponse.badRequest()
            .message("아이디 및 패스워드는 필수 입력입니다.")
            .build());
      }

      UsernamePasswordAuthenticationToken token =
          new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

      return this.getAuthenticationManager().authenticate(token);

    } catch (IOException e) {
      e.printStackTrace();
      throw new BasicAuthenticationServiceException(ApiErrorResponse.badRequest()
          .build());

    }

  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    SecurityContextHolder.getContext().setAuthentication(authResult);
    this.basicAuthenticationResultHandler.onAuthenticationSuccess(request, response, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    this.basicAuthenticationResultHandler.onAuthenticationFailure(request, response, failed);
  }
}
