package com.github.hoyoung.security.authentication.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hoyoung.security.model.UserDetailsContext;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * Created by HoYoung on 2021/02/16.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
  private static final long serialVersionUID = 2877954820905567501L;

  private UserDetailsContext userDetailsContext;
  private DecodedJWT token;

  public JwtAuthenticationToken(DecodedJWT token) {
    super(null);
    this.setAuthenticated(false);
    this.token = token;
  }

  public JwtAuthenticationToken(UserDetailsContext userDetailsContext, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.eraseCredentials();
    this.userDetailsContext = userDetailsContext;
    super.setAuthenticated(true);
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (authenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }

    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return this.token;
  }

  @Override
  public Object getPrincipal() {
    return this.userDetailsContext;
  }

  @Override
  public void eraseCredentials() {
    super.eraseCredentials();

  }
}
