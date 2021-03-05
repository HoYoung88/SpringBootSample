package com.github.hoyoung.security.generate.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.hoyoung.config.security.properties.JwtProperties;
import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.security.exception.BasicAuthenticationServiceException;
import com.github.hoyoung.security.model.UserDetailsContext;
import com.github.hoyoung.util.DateUtils;
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

  public String createAccessToken(UserDetailsContext userDetailsContext) {
    Algorithm algorithmHS = this.getAlogrithm();
    return JWT.create()
        .withIssuer(jwtProperties.getTokenIssuer())
        .withExpiresAt(DateUtils.asDate(userDetailsContext.getLoginTime()
            .plusMinutes(jwtProperties.getTokenExpirationTime())))
        .withSubject(userDetailsContext.getUsername())
        .withArrayClaim("scopes", userDetailsContext.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toArray(String[]::new))
        .sign(algorithmHS);
  }

  public String createRefreshToken(UserDetailsContext userDetailsContext) {
    Algorithm algorithmHS = this.getAlogrithm();
    return JWT.create()
        .withIssuer(jwtProperties.getTokenIssuer())
        .withNotBefore(DateUtils.asDate(userDetailsContext.getLoginTime()
            .plusMinutes(jwtProperties.getRefreshTokenExpTime())))
        .withExpiresAt(DateUtils.asDate(userDetailsContext.getLoginTime()
            .plusMinutes(jwtProperties.getRefreshTokenExpTime())))
        .withSubject(userDetailsContext.getUsername())
        .withArrayClaim("scopes", userDetailsContext.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toArray(String[]::new))
        .sign(algorithmHS);
  }

  public DecodedJWT vaildToken(String jwtToken) throws AuthenticationServiceException {
    JWTVerifier verifier = JWT.require(this.getAlogrithm()).build();
    try {
      return verifier.verify(jwtToken);
    } catch (TokenExpiredException token) {
      throw new BasicAuthenticationServiceException(ApiErrorResponse.unauthorized()
          .message("만료된 Token입니다.")
          .build());
    } catch (JWTVerificationException exception){
      exception.printStackTrace();
      throw new AuthenticationServiceException(exception.getMessage());
    }
  }

  public JwtProperties getProperties() {
    return jwtProperties;
  }
}
