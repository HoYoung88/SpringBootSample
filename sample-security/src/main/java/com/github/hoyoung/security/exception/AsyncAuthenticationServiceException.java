package com.github.hoyoung.security.exception;

import com.github.hoyoung.web.status.service.ServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Created by HoYoung on 2021/02/15.
 */
@Getter
public class AsyncAuthenticationServiceException extends AuthenticationServiceException {
  private HttpStatus status = HttpStatus.UNAUTHORIZED;
  private final ServiceStatus serviceStatus;

  public AsyncAuthenticationServiceException(ServiceStatus serviceStatus) {
    super(serviceStatus.getMessage());
    this.serviceStatus = serviceStatus;
  }

  public AsyncAuthenticationServiceException(ServiceStatus serviceStatus, HttpStatus status) {
    super(serviceStatus.getMessage());
    this.serviceStatus = serviceStatus;
    this.status = status;
  }

  public AsyncAuthenticationServiceException(ServiceStatus serviceStatus, String message, HttpStatus status) {
    super(message);
    this.serviceStatus = serviceStatus;
    this.status = status;
  }





}
