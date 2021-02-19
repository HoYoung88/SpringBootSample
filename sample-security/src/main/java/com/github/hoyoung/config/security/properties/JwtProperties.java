package com.github.hoyoung.config.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by HoYoung on 2021/02/18.
 */
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtProperties {
  private Integer tokenExpirationTime;
  private String tokenIssuer;
  private String tokenSubject;
  private String tokenSigningKey;
  private Integer refreshTokenExpTime;
}
