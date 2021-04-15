package com.github.hoyoung.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by HoYoung on 2021/04/08.
 */
@Getter
public class SuccessResponse<T> {
  private final String code;
  private final String message;
  @JsonInclude(Include.NON_EMPTY)
  private final T data;

  @Builder
  public SuccessResponse(String code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> SuccessResponse<T> withData(T data) {
    return new SuccessResponse<>(String.valueOf(HttpStatus.OK.value()),
        "정상 처리 되었습니다.",
        data);
  }
}
