package com.github.hoyoung.security.handler;

import com.github.hoyoung.security.model.UserContext;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Component
@Slf4j
public class BasicAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.warn("onAuthenticationSuccess >>> ");
    UserContext userContext = (UserContext) authentication.getPrincipal();
    log.warn("getUsername :: {}", userContext.getUsername());
    log.warn("getAuthorities :: {}", userContext.getAuthorities());

  }
}
