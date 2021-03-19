package com.github.hoyoung.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.github.hoyoung.web.status.service.ErrorServiceStatus;
import com.github.hoyoung.web.status.service.BasicServiceStatus;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Created by HoYoung on 2021/01/21.
 */
@Getter
@JsonRootName("errors")
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

  @JsonInclude(Include.NON_EMPTY)
  private final String method;

  @JsonInclude(Include.NON_EMPTY)
  private final String contentType;

  protected ApiErrorResponse(HttpHeaders headers,
      HttpStatus status,
      BasicServiceStatus basicServiceStatus,
      String message,
      String field,
      String path,
      String method,
      String contentType) {
    this.headers = headers;
    this.status = status;
    this.serviceStatus = basicServiceStatus.getCode();
    this.serviceStatusDesc = basicServiceStatus.name();
    this.field = field;
    this.path = path;
    this.message = message;
    this.method = method;
    this.contentType = contentType;
  }

  public static ApiErrorResponseBuilder builder(HttpHeaders headers, HttpStatus status,
      BasicServiceStatus basicServiceStatus) {
    return new ApiErrorResponseBuilder(headers, status, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder builder(HttpStatus status, BasicServiceStatus basicServiceStatus) {
    return builder(new HttpHeaders(), status, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder badRequest(BasicServiceStatus basicServiceStatus) {
    return builder(HttpStatus.BAD_REQUEST, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder unauthorized(BasicServiceStatus basicServiceStatus) {
    return builder(HttpStatus.UNAUTHORIZED, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder forbidden(BasicServiceStatus basicServiceStatus) {
    return builder(HttpStatus.FORBIDDEN, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder notFound(BasicServiceStatus basicServiceStatus) {
    return builder(HttpStatus.NOT_FOUND, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder methodNotAllowed(BasicServiceStatus basicServiceStatus) {
    return builder(HttpStatus.METHOD_NOT_ALLOWED, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder unsupportedMediaType(BasicServiceStatus basicServiceStatus) {
    return builder(HttpStatus.UNSUPPORTED_MEDIA_TYPE, basicServiceStatus);
  }

  public static ApiErrorResponseBuilder badRequest() {
    return badRequest(ErrorServiceStatus.BAD_REQUEST);
  }

  public static ApiErrorResponseBuilder unauthorized() {
    return unauthorized(ErrorServiceStatus.UNAUTHORIZED);
  }

  public static ApiErrorResponseBuilder forbidden() {
    return forbidden(ErrorServiceStatus.FORBIDDEN);
  }

  public static ApiErrorResponseBuilder notFound() {
    return notFound(ErrorServiceStatus.NOT_FOUND);
  }

  public static ApiErrorResponseBuilder methodNotAllowed() {
    return methodNotAllowed(ErrorServiceStatus.METHOD_NOT_ALLOWED);
  }

  public static ApiErrorResponseBuilder unsupportedMediaType() {
    return unsupportedMediaType(ErrorServiceStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  public static class ApiErrorResponseBuilder {
    private final HttpHeaders headers;
    private final HttpStatus status;
    private final BasicServiceStatus basicServiceStatus;
    private String message;
    private String field;
    private String path;
    private String method;
    private String contentType;

    public ApiErrorResponseBuilder(HttpHeaders headers,
        HttpStatus status,
        BasicServiceStatus basicServiceStatus) {
      this.headers = headers;
      this.status = status;
      this.basicServiceStatus = basicServiceStatus;
      this.message = basicServiceStatus.getMessage();
    }

    public ApiErrorResponseBuilder(HttpStatus status,
        BasicServiceStatus basicServiceStatus) {
      this.headers = new HttpHeaders();
      this.status = status;
      this.basicServiceStatus = basicServiceStatus;
      this.message = basicServiceStatus.getMessage();
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

    public ApiErrorResponseBuilder method(String method) {
      this.method = method;
      return this;
    }

    public ApiErrorResponseBuilder contentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    public ApiErrorResponse build() {
      return new ApiErrorResponse(this.headers,
          this.status,
          this.basicServiceStatus,
          this.message,
          this.field,
          this.path,
          this.method,
          this.contentType);
    }

  }

}
