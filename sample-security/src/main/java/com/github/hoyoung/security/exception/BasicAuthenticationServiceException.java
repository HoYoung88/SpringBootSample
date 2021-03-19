package com.github.hoyoung.security.exception;

import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.web.status.service.BasicServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Getter
public class BasicAuthenticationServiceException extends AuthenticationServiceException {
  private HttpStatus status = HttpStatus.UNAUTHORIZED;
  private BasicServiceStatus basicServiceStatus;
  private ApiErrorResponse apiErrorResponse;

  public BasicAuthenticationServiceException(BasicServiceStatus basicServiceStatus) {
    super(basicServiceStatus.getMessage());
    this.basicServiceStatus = basicServiceStatus;
  }

  public BasicAuthenticationServiceException(BasicServiceStatus basicServiceStatus, HttpStatus status) {
    super(basicServiceStatus.getMessage());
    this.basicServiceStatus = basicServiceStatus;
    this.status = status;
  }

  public BasicAuthenticationServiceException(BasicServiceStatus basicServiceStatus, String message, HttpStatus status) {
    super(message);
    this.basicServiceStatus = basicServiceStatus;
    this.status = status;
  }

  public BasicAuthenticationServiceException(ApiErrorResponse apiErrorResponse) {
    super("");
    this.apiErrorResponse = apiErrorResponse;
  }


}
