package com.github.hoyoung.security.generate.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hoyoung.config.security.properties.JwtProperties;
import com.github.hoyoung.security.model.UserContext;
import com.github.hoyoung.util.DateUtils;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/16.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenGenerate {

  private final JwtProperties jwtProperties;

  private Algorithm getAlogrithm() {
    return Algorithm.HMAC256(jwtProperties.getTokenSigningKey());
  }

  public String createAccessToken(UserContext userContext) {
    Algorithm algorithmHS = this.getAlogrithm();
    return JWT.create()
        .withIssuer(jwtProperties.getTokenIssuer())
        .withExpiresAt(DateUtils.asDate(LocalDateTime.now()
            .plusMinutes(jwtProperties.getTokenExpirationTime())))
        .withSubject(userContext.getUsername())
        .withClaim("id", UUID.randomUUID().toString())
        .withClaim("email", userContext.getUsername())
        .withArrayClaim("scopes", userContext.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toArray(String[]::new))
        .sign(algorithmHS);
  }

  public DecodedJWT vaildToken(String jwtToken) throws AuthenticationServiceException {
    JWTVerifier verifier = JWT.require(this.getAlogrithm()).build();
    try {
      return verifier.verify(jwtToken);
    } catch (JWTVerificationException exception){
      exception.printStackTrace();
      throw new AuthenticationServiceException(exception.getMessage());
    }
  }

  public JwtProperties getProperties() {
    return jwtProperties;
  }
}
