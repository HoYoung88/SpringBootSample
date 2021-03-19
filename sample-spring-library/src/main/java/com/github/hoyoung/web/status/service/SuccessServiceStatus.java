package com.github.hoyoung.web.status.service;

import lombok.Getter;

/**
 * Created by HoYoung on 2021/03/12.
 */
@Getter
public enum SuccessServiceStatus implements BasicServiceStatus {
  SUCCESS("200", "요청이 정상적으로 처리되었습니다."),
  CREATED("201", "정상적으로 생성되었습니다.");

  private final String code;
  private final String message;

  SuccessServiceStatus(final String code, final String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String format(Object... args) {
    return String.format(this.message, args);
  };
}
