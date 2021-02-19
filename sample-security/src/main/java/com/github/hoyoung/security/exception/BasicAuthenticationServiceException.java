package com.github.hoyoung.security.exception;

import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.web.status.service.ServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Getter
public class BasicAuthenticationServiceException extends AuthenticationServiceException {
  private HttpStatus status = HttpStatus.UNAUTHORIZED;
  private ServiceStatus serviceStatus;
  private ApiErrorResponse apiErrorResponse;

  public BasicAuthenticationServiceException(ServiceStatus serviceStatus) {
    super(serviceStatus.getMessage());
    this.serviceStatus = serviceStatus;
  }

  public BasicAuthenticationServiceException(ServiceStatus serviceStatus, HttpStatus status) {
    super(serviceStatus.getMessage());
    this.serviceStatus = serviceStatus;
    this.status = status;
  }

  public BasicAuthenticationServiceException(ServiceStatus serviceStatus, String message, HttpStatus status) {
    super(message);
    this.serviceStatus = serviceStatus;
    this.status = status;
  }

  public BasicAuthenticationServiceException(ApiErrorResponse apiErrorResponse) {
    super("");
    this.apiErrorResponse = apiErrorResponse;
  }


}
