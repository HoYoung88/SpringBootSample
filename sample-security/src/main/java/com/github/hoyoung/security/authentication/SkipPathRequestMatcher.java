package com.github.hoyoung.security.authentication;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Created by HoYoung on 2021/02/19.
 */
public class SkipPathRequestMatcher implements RequestMatcher {
  private OrRequestMatcher matchers;
  private RequestMatcher processingMatcher;

  public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
    List<RequestMatcher> m = pathsToSkip.stream().map(AntPathRequestMatcher::new).collect(
        Collectors.toList());
    matchers = new OrRequestMatcher(m);
    processingMatcher = new AntPathRequestMatcher(processingPath);
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    if (matchers.matches(request)) {
      return false;
    }
    return processingMatcher.matches(request);
  }

}
