package com.github.hoyoung.model.response;


import com.github.hoyoung.web.status.service.BaseErrorServiceStatus;
import com.github.hoyoung.web.status.service.ServiceStatus;
import lombok.Getter;

/**
 * Created by HoYoung on 2021/01/25.
 */
@Getter
public class ApiSuccessResponse<T> {

  private final String code;
  private final String message;
  private final T data;

  private ApiSuccessResponse(ServiceStatus serviceStatus, String message, T data) {
    this.code = serviceStatus.getCode();
    this.data = data;
    this.message = message;
  }

  public static <T> ApiSuccessResponseBuilder<T> builder() {
    return new ApiSuccessResponseBuilder<T>(BaseErrorServiceStatus.SUCCESS);
  }

  public static <T> ApiSuccessResponseBuilder<T> builder(ServiceStatus serviceStatus) {
    return new ApiSuccessResponseBuilder<T>(serviceStatus);
  }

  public static <T> ApiSuccessResponseBuilder<T> builder(ServiceStatus serviceStatus, String message) {
    return new ApiSuccessResponseBuilder<T>(serviceStatus, message);
  }

  public static class ApiSuccessResponseBuilder<T> {
    private final ServiceStatus serviceStatus;
    private final String message;
    private T data;

    private ApiSuccessResponseBuilder(ServiceStatus serviceStatus) {
      this.serviceStatus = serviceStatus;
      this.message = serviceStatus.getMessage();
    }

    private ApiSuccessResponseBuilder(ServiceStatus serviceStatus, String message) {
      this.serviceStatus = serviceStatus;
      this.message = message;
    }

    public ApiSuccessResponseBuilder<T> data(final T data) {
      this.data = data;
      return this;
    }
    public ApiSuccessResponse<T> build() {
      return new ApiSuccessResponse<T>(this.serviceStatus, this.message, this.data);
    }
  }

}
