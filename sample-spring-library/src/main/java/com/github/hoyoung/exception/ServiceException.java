package com.github.hoyoung.exception;

import com.github.hoyoung.web.status.service.ServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Created by HoYoung on 2021/01/27.
 */
@Getter
public class ServiceException extends RuntimeException {
  private HttpHeaders headers = new HttpHeaders();
  private HttpStatus status = HttpStatus.BAD_REQUEST;
  private final ServiceStatus serviceStatus;

  public ServiceException(ServiceStatus serviceStatus) {
    super(serviceStatus.getMessage());
    this.serviceStatus = serviceStatus;
  }

  public ServiceException(ServiceStatus serviceStatus, String message) {
    super(message);
    this.serviceStatus = serviceStatus;
  }

  public ServiceException(HttpStatus status, ServiceStatus serviceStatus) {
    super(serviceStatus.getMessage());
    this.status = status;
    this.serviceStatus = serviceStatus;
  }

  public ServiceException(HttpStatus status, ServiceStatus serviceStatus, String message) {
    super(message);
    this.status = status;
    this.serviceStatus = serviceStatus;
  }

  public ServiceException(HttpHeaders headers, HttpStatus status, ServiceStatus serviceStatus) {
    super(serviceStatus.getMessage());
    this.headers = headers;
    this.status = status;
    this.serviceStatus = serviceStatus;
  }

  public ServiceException(HttpHeaders headers, HttpStatus status, ServiceStatus serviceStatus, String message) {
    super(message);
    this.headers = headers;
    this.status = status;
    this.serviceStatus = serviceStatus;
  }
}
