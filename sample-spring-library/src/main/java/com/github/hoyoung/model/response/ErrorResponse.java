package com.github.hoyoung.model.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by HoYoung on 2021/04/07.
 */
@Getter
public class ErrorResponse {
  private final String error;
  private final String errorDesc;
  private final String message;

  @Builder
  public ErrorResponse(String error, String errorDesc, String message) {
    this.error = error;
    this.errorDesc = errorDesc;
    this.message = message;
  }

  public static ErrorResponse badRequest(String message) {
    return ErrorResponse.builder()
        .error(String.valueOf(HttpStatus.BAD_REQUEST.value()))
        .errorDesc(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(message)
        .build();
  }

  public static ErrorResponse methodNotAllowed(String message) {
    return ErrorResponse.builder()
        .error(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()))
        .errorDesc(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
        .message(message)
        .build();
  }

  public static ErrorResponse unsupportedMediaType(String message) {
    return ErrorResponse.builder()
        .error(String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()))
        .errorDesc(HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())
        .message(message)
        .build();
  }

  public static ErrorResponse notFound(String message) {
    return ErrorResponse.builder()
        .error(String.valueOf(HttpStatus.NOT_FOUND.value()))
        .errorDesc(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(message)
        .build();
  }

  public static ErrorResponse internalServerError(String message) {
    return ErrorResponse.builder()
        .error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .errorDesc(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message(message)
        .build();
  }
}
