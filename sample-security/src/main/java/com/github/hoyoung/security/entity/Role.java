package com.github.hoyoung.security.entity;

/**
 * Created by HoYoung on 2021/02/16.
 */
public enum Role {
  ADMIN, USER;

  public String authority() {
    return "ROLE_" + this.name();
  }
}
