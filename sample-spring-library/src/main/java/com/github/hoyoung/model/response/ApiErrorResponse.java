package com.github.hoyoung.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.hoyoung.web.status.service.ServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Created by HoYoung on 2021/01/21.
 */
@Getter
@JsonPropertyOrder(value = {"serviceStatus", "serviceStatusDesc", "field", "message"})
public class ApiErrorResponse {

  @JsonIgnore
  private final HttpHeaders headers;

  @JsonIgnore
  private final HttpStatus status;

  @JsonProperty(value = "error")
  private final String serviceStatus;

  @JsonProperty(value = "error_desc")
  private final String serviceStatusDesc;
  private final String message;

  @JsonInclude(Include.NON_EMPTY)
  private final String field;

  @JsonInclude(Include.NON_EMPTY)
  private final String path;

  protected ApiErrorResponse(HttpHeaders headers,
      HttpStatus status,
      ServiceStatus serviceStatus,
      String message,
      String field,
      String path) {
    this.headers = headers;
    this.status = status;
    this.serviceStatus = serviceStatus.getCode();
    this.serviceStatusDesc = serviceStatus.name();
    this.message = message;
    this.field = field;
    this.path = path;
  }

  public static ApiErrorResponseBuilder builder(HttpHeaders headers, HttpStatus status,
      ServiceStatus serviceStatus) {
    return new ApiErrorResponseBuilder(headers, status, serviceStatus);
  }

  public static ApiErrorResponseBuilder builder(HttpStatus status, ServiceStatus serviceStatus) {
    return builder(new HttpHeaders(), status, serviceStatus);
  }

  public static ApiErrorResponseBuilder unauthorized(ServiceStatus serviceStatus) {
    return builder(HttpStatus.UNAUTHORIZED, serviceStatus);
  }

  public static ApiErrorResponseBuilder forbidden(ServiceStatus serviceStatus) {
    return builder(HttpStatus.FORBIDDEN, serviceStatus);
  }

  public static ApiErrorResponseBuilder notFound(ServiceStatus serviceStatus) {
    return builder(HttpStatus.NOT_FOUND, serviceStatus);
  }

  public static ApiErrorResponseBuilder badRequest(ServiceStatus serviceStatus) {
    return builder(HttpStatus.BAD_REQUEST, serviceStatus);
  }

  public static class ApiErrorResponseBuilder {
    private final HttpHeaders headers;
    private final HttpStatus status;
    private final ServiceStatus serviceStatus;
    private String message;
    private String field;
    private String path;

    public ApiErrorResponseBuilder(HttpHeaders headers,
        HttpStatus status,
        ServiceStatus serviceStatus) {
      this.headers = headers;
      this.status = status;
      this.serviceStatus = serviceStatus;
      this.message = serviceStatus.getMessage();
    }

    public ApiErrorResponseBuilder(HttpStatus status,
        ServiceStatus serviceStatus) {
      this.headers = new HttpHeaders();
      this.status = status;
      this.serviceStatus = serviceStatus;
      this.message = serviceStatus.getMessage();
    }

    public ApiErrorResponseBuilder message(String message) {
      this.message = message;
      return this;
    }

    public ApiErrorResponseBuilder field(String field) {
      this.field = field;
      return this;
    }

    public ApiErrorResponseBuilder path(String path) {
      this.path = path;
      return this;
    }

    public ApiErrorResponse build() {
      return new ApiErrorResponse(this.headers,
          this.status,
          this.serviceStatus,
          this.message,
          this.field,
          this.path);
    }

  }

}
