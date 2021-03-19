package com.github.hoyoung.exception;

import com.github.hoyoung.web.status.service.BasicServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Created by HoYoung on 2021/01/27.
 */
@Getter
public class ApiServiceException extends RuntimeException {
  private HttpHeaders headers = new HttpHeaders();
  private HttpStatus status = HttpStatus.BAD_REQUEST;
  private final BasicServiceStatus basicServiceStatus;

  public ApiServiceException(BasicServiceStatus basicServiceStatus) {
    super(basicServiceStatus.getMessage());
    this.basicServiceStatus = basicServiceStatus;
  }

  public ApiServiceException(BasicServiceStatus basicServiceStatus, String message) {
    super(message);
    this.basicServiceStatus = basicServiceStatus;
  }

  public ApiServiceException(HttpStatus status, BasicServiceStatus basicServiceStatus) {
    super(basicServiceStatus.getMessage());
    this.status = status;
    this.basicServiceStatus = basicServiceStatus;
  }

  public ApiServiceException(HttpStatus status, BasicServiceStatus basicServiceStatus, String message) {
    super(message);
    this.status = status;
    this.basicServiceStatus = basicServiceStatus;
  }

  public ApiServiceException(HttpHeaders headers, HttpStatus status, BasicServiceStatus basicServiceStatus) {
    super(basicServiceStatus.getMessage());
    this.headers = headers;
    this.status = status;
    this.basicServiceStatus = basicServiceStatus;
  }

  public ApiServiceException(HttpHeaders headers, HttpStatus status, BasicServiceStatus basicServiceStatus, String message) {
    super(message);
    this.headers = headers;
    this.status = status;
    this.basicServiceStatus = basicServiceStatus;
  }
}
