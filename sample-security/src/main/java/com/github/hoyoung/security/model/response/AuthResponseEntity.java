package com.github.hoyoung.security.model.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hoyoung.model.response.ApiSuccessResponse;
import com.github.hoyoung.security.generate.jwt.JwtTokenGenerate;
import com.github.hoyoung.security.model.UserDetailsContext;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 * Created by HoYoung on 2021/02/22.
 */
@Getter
public class AuthResponseEntity {
  private final static String HEADER_ACCESS_TOKEN_NAME = "X-ACCESS-TOKEN";
  private final static String HEADER_REFRESH_TOKEN_NAME = "X-REFRESH-TOKEN";

  private final String accessToken;
  private final String refreshToken;

  @Builder
  public AuthResponseEntity(String accessToken, String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public static void body(HttpServletRequest request, HttpServletResponse response,
      UserDetailsContext userDetailsContext, JwtTokenGenerate jwtTokenGenerate)
      throws IOException {

    ///로그인 성공한 시간 기준으로 JWT ExpiresAt 생성
    userDetailsContext.addLoginSuccessTime();
    String accessToken = jwtTokenGenerate.createAccessToken(userDetailsContext);
    String refreshToken = jwtTokenGenerate.createRefreshToken(userDetailsContext);

    //쿠키에 담기
    Cookie accessTokenCookie = new Cookie(HEADER_ACCESS_TOKEN_NAME, accessToken);
    accessTokenCookie.setMaxAge(jwtTokenGenerate.getProperties().getTokenExpirationTime() * 60);
    accessTokenCookie.setPath("/");
    accessTokenCookie.setHttpOnly(true);
    response.addCookie(accessTokenCookie);

    Cookie refreshTokenCookie = new Cookie(HEADER_REFRESH_TOKEN_NAME, refreshToken);
    refreshTokenCookie.setMaxAge(jwtTokenGenerate.getProperties().getTokenExpirationTime() * 60 + 10);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setHttpOnly(true);
    response.addCookie(refreshTokenCookie);

    response.setHeader(HEADER_ACCESS_TOKEN_NAME, accessToken);
    response.setHeader(HEADER_REFRESH_TOKEN_NAME, refreshToken);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value());

    AuthResponseEntity authResponseEntity = AuthResponseEntity.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();

    try (OutputStream os = response.getOutputStream()) {
      new ObjectMapper().writeValue(os, ApiSuccessResponse.ok(authResponseEntity));
      os.flush();
    }
  }

}
