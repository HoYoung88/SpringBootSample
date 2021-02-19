package com.github.hoyoung.security.authentication.filter.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.security.authentication.token.JwtAuthenticationToken;
import com.github.hoyoung.security.exception.BasicAuthenticationServiceException;
import com.github.hoyoung.security.generate.jwt.JwtTokenGenerate;
import com.github.hoyoung.security.handler.BasicAuthenticationResultHandler;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Slf4j
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
  public static String HEADER_PREFIX = "Bearer ";

  private JwtTokenGenerate jwtTokenGenerate;
  private BasicAuthenticationResultHandler basicAuthenticationResultHandler;

  public JwtAuthenticationProcessingFilter(
      RequestMatcher requiresAuthenticationRequestMatcher) {
    super(requiresAuthenticationRequestMatcher);
  }

  public void setBasicAuthenticationResultHandler(
      BasicAuthenticationResultHandler basicAuthenticationResultHandler) {
    this.basicAuthenticationResultHandler = basicAuthenticationResultHandler;
  }

  public void setJwtTokenGenerate(JwtTokenGenerate jwtTokenGenerate) {
    this.jwtTokenGenerate = jwtTokenGenerate;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.warn("JwtAuthenticationProcessingFilter >>>> ");

    String jwtToken = this.extract(request.getHeader(HttpHeaders.AUTHORIZATION));

    log.warn("jwtToken :: {}", jwtToken);
    DecodedJWT token = this.jwtTokenGenerate.vaildToken(jwtToken);

    return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authResult);
    SecurityContextHolder.setContext(context);
    chain.doFilter(request, response);
//    this.basicAuthenticationResultHandler.onAuthenticationSuccess(request, response, authResult);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    SecurityContextHolder.clearContext();
    this.basicAuthenticationResultHandler.onAuthenticationFailure(request, response, failed);
  }

  private String extract(String header) {
    if (StringUtils.isBlank(header)) {
      throw new BasicAuthenticationServiceException(ApiErrorResponse.badRequest()
          .message("인증 요청정보를 확인해주세요.")
          .build());
    }

    if (header.length() < HEADER_PREFIX.length()) {
      throw new BasicAuthenticationServiceException(ApiErrorResponse.badRequest()
          .message("인증 요청정보를 확인해주세요.")
          .build());
    }

    return header.substring(HEADER_PREFIX.length(), header.length());
  }
}
