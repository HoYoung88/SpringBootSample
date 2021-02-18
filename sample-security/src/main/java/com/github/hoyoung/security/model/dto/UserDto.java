package com.github.hoyoung.security.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Getter
public class UserDto {
  private final String email;
  private final String password;

  @JsonCreator
  public UserDto(
      @JsonProperty("email") String email,
      @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }
}
