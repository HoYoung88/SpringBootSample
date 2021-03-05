package com.github.hoyoung.web.status.service;

import lombok.Getter;

/**
 * Created by HoYoung on 2021/01/27.
 */
@Getter
public enum BaseErrorServiceStatus implements ServiceStatus {
  SUCCESS("200", "요청이 정상적으로 처리되었습니다."),
  BAD_REQUEST("-400", "올바르지 않는 요청입니다."),
  UNAUTHORIZED("-401", "로그인 정보가 올바르지 않습니다."),
  FORBIDDEN("-403", "접근이 거부되었습니다."),
  NOT_FOUND("-404", "요청하신 정보를 찾을 수 없습니다."),
  METHOD_NOT_ALLOWED("-405", "허용되지 않는 Method입니다."),
  NOT_ACCEPTABLE("-406", "허용되지 않는 콘텐츠 특성입니다."),
  UNSUPPORTED_MEDIA_TYPE("-415", "지원하지 않는 Media Type입니다."),
  HTTP_MESSAGE_NOT_WRITABLE("-400", "올바르지 않는 요청입니다."),
  HTTP_MESSAGE_NOT_READABLE("-400", "올바르지 않는 요청입니다."),
  INTERNAL_SERVER_ERROR("-500", "서버 오류입니다.");

  private final String code;
  private final String message;

  BaseErrorServiceStatus(final String code, final String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String format(Object... args) {
    return String.format(this.message, args);
  };
}
