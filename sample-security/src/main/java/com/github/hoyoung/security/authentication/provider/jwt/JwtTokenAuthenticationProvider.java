package com.github.hoyoung.security.authentication.provider.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hoyoung.security.authentication.token.JwtAuthenticationToken;
import com.github.hoyoung.security.model.UserContext;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/16.
 */
@Slf4j
@Component
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    log.warn("{}", authentication.getCredentials());
    log.warn("{}", authentication.getAuthorities());
    log.warn("{}", authentication.getPrincipal());

    DecodedJWT jwtToken = (DecodedJWT) authentication.getCredentials();

    List<String> scopes = jwtToken.getClaim("scopes").asList(String.class);

    List<GrantedAuthority> authorities = scopes.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());

    UserContext context = UserContext.withUsername(jwtToken.getSubject())
        .authorities(authorities).build();

    return new JwtAuthenticationToken(context, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
  }
}