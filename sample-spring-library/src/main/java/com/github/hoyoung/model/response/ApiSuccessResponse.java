package com.github.hoyoung.model.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.github.hoyoung.web.status.service.SuccessServiceStatus;
import com.github.hoyoung.web.status.service.BasicServiceStatus;
import java.net.URI;
import lombok.Getter;

/**
 * Created by HoYoung on 2021/01/25.
 */
@Getter
@JsonRootName(value = "result")
public class ApiSuccessResponse<T> {

  private final String code;
  private final String message;
  @JsonInclude(Include.NON_EMPTY)
  private final URI location;

  @JsonInclude(Include.NON_EMPTY)
  private final T data;

  private ApiSuccessResponse(BasicServiceStatus basicServiceStatus, String message, T data, URI location) {
    this.code = basicServiceStatus.getCode();
    this.data = data;
    this.message = message;
    this.location = location;
  }

  public static ApiSuccessResponse<Object> create(URI location) {
    return new ApiSuccessResponseBuilder<>(SuccessServiceStatus.CREATED)
        .location(location)
        .build();
  }

  public static <T> ApiSuccessResponse<T> ok(T data) {
    return new ApiSuccessResponseBuilder<T>(SuccessServiceStatus.SUCCESS)
        .data(data).build();
  }

  public static class ApiSuccessResponseBuilder<T> {
    private final BasicServiceStatus basicServiceStatus;
    private final String message;
    private T data;
    private URI location;

    private ApiSuccessResponseBuilder(BasicServiceStatus basicServiceStatus) {
      this.basicServiceStatus = basicServiceStatus;
      this.message = basicServiceStatus.getMessage();
    }

    private ApiSuccessResponseBuilder(BasicServiceStatus basicServiceStatus, String message) {
      this.basicServiceStatus = basicServiceStatus;
      this.message = message;
    }

    public ApiSuccessResponseBuilder<T> data(final T data) {
      this.data = data;
      return this;
    }

    public ApiSuccessResponseBuilder<T> location(final URI location) {
      this.location = location;
      return this;
    }

    public ApiSuccessResponse<T> build() {
      return new ApiSuccessResponse<T>(this.basicServiceStatus, this.message, this.data, this.location);
    }
  }
}
