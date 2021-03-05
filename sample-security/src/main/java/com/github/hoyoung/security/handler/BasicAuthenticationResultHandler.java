package com.github.hoyoung.security.handler;

import com.github.hoyoung.security.generate.jwt.JwtTokenGenerate;
import com.github.hoyoung.security.model.UserDetailsContext;
import com.github.hoyoung.security.model.response.AuthErrorResponseEntity;
import com.github.hoyoung.security.model.response.AuthResponseEntity;
import com.github.hoyoung.security.user.repository.UserRepository;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
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
  private final UserRepository userRepository;
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.warn("===== onAuthenticationFailure :: {}", exception.getMessage());
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

    UserDetailsContext userDetailsContext = null;
    if(authentication instanceof OAuth2AuthenticationToken) {
      OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
      log.warn("name: {}", oAuth2User.getName());
      log.warn("authorities: {}", oAuth2User.getAuthorities());
      log.warn("getAttributes: {}", oAuth2User.getAttributes());
      log.warn("ID: {}", ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId());
      userDetailsContext = UserDetailsContext.builder().build();
    } else {
      userDetailsContext = (UserDetailsContext) authentication.getPrincipal();
      log.warn("getUsername :: {}", userDetailsContext.getUsername());
      log.warn("getAuthorities :: {}", userDetailsContext.getAuthorities());
    }

//    final LoginUser loginUser = this.userRepository.findByUsername(userContext.getUsername())
//        .orElseThrow(() -> new BasicAuthenticationServiceException(
//            ApiErrorResponse.builder(HttpStatus.INTERNAL_SERVER_ERROR,
//                BaseErrorServiceStatus.INTERNAL_SERVER_ERROR).build()));

    AuthResponseEntity.body(request, response, userDetailsContext, jwtTokenGenerate);
    clearAuthenticationAttributes(request);

  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.warn("===== onLogoutSuccess >>> ");
    SecurityContextHolder.clearContext();

    response.setStatus(HttpStatus.OK.value());
  }

  protected final void clearAuthenticationAttributes(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
  }
}
