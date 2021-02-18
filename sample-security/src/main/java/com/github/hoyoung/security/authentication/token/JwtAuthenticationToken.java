package com.github.hoyoung.security.authentication.token;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by HoYoung on 2021/02/16.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

  public JwtAuthenticationToken(
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();

  }
}
