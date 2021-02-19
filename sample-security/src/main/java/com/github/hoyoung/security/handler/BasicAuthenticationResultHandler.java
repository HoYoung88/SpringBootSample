package com.github.hoyoung.security.handler;

import com.github.hoyoung.security.generate.jwt.JwtTokenGenerate;
import com.github.hoyoung.security.model.UserContext;
import com.github.hoyoung.security.model.response.AuthErrorResponseEntity;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/18.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BasicAuthenticationResultHandler implements AuthenticationSuccessHandler,
    AuthenticationFailureHandler, LogoutSuccessHandler {

  private final JwtTokenGenerate jwtTokenGenerate;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.warn("===== onAuthenticationFailure :: {}", exception.getMessage());
    log.warn("===== onAuthenticationFailure :: {} :: {}", request.getRequestURI(), request.getMethod());
    AuthErrorResponseEntity.body(request, response, exception);
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException, ServletException {
    this.onAuthenticationSuccess(request, response, authentication);
    chain.doFilter(request, response);
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.warn("===== onAuthenticationSuccess >>> ");
    log.warn("{}", authentication.getClass().getName());
    log.warn("{}", authentication.toString());
    log.warn("{}", request);
//    UserContext userContext = (UserContext) authentication.getPrincipal();
//    log.warn("getUsername :: {}", userContext.getUsername());
//    log.warn("getAuthorities :: {}", userContext.getAuthorities());
//    String token = this.jwtTokenGenerate.createAccessToken(userContext);
//    log.warn("Token -> {}", token);
//
//    Cookie cookie = new Cookie("X-ACCESS-TOKEN", token);
//    cookie.setMaxAge(this.jwtTokenGenerate.getProperties().getTokenExpirationTime() * 60);
//    cookie.setPath("/");
//    cookie.setHttpOnly(true);
//    response.addCookie(cookie);
//    response.addHeader("X-ACCESS-TOKEN", token);
//    response.sendRedirect("/");
    if(authentication instanceof OAuth2AuthenticationToken) {
      OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
      log.warn("name: {}", oAuth2User.getName());
      log.warn("authorities: {}", oAuth2User.getAuthorities());
      log.warn("getAttributes: {}", oAuth2User.getAttributes());
      log.warn("ID: {}", ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId());
    } else {
      UserContext userContext = (UserContext) authentication.getPrincipal();
      log.warn("getUsername :: {}", userContext.getUsername());
      log.warn("getAuthorities :: {}", userContext.getAuthorities());
      String token = this.jwtTokenGenerate.createAccessToken(userContext);
      log.warn("Token -> {}", token);

      Cookie cookie = new Cookie("X-ACCESS-TOKEN", token);
      cookie.setMaxAge(this.jwtTokenGenerate.getProperties().getTokenExpirationTime() * 60);
      cookie.setPath("/");
      cookie.setHttpOnly(true);
      response.addCookie(cookie);
      response.addHeader("X-ACCESS-TOKEN", token);
    }


    clearAuthenticationAttributes(request);

  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.warn("===== onLogoutSuccess >>> ");
    SecurityContextHolder.clearContext();
    response.sendRedirect("/login");
  }

  protected final void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
  }
}
