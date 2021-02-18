package com.github.hoyoung.advice;


import static com.github.hoyoung.web.status.service.BaseServiceStatus.*;

import com.github.hoyoung.exception.ServiceException;
import com.github.hoyoung.model.response.ApiErrorResponse;
import com.github.hoyoung.web.status.service.BaseServiceStatus;
import java.util.Objects;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Created by HoYoung on 2021/01/27.
 */
@RestControllerAdvice
@Slf4j
public class ResponseEntityExceptionAdvice extends ResponseEntityExceptionHandler {
  private ApiErrorResponse response;

  public ResponseEntityExceptionAdvice() {
    super();
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    super.handleHttpRequestMethodNotSupported(ex, headers, status, request);

    String methodNotAllowed = ex.getMethod();
    this.response = ApiErrorResponse.builder(headers, status, METHOD_NOT_ALLOWED)
        .message(METHOD_NOT_ALLOWED.format(methodNotAllowed))
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    super.handleHttpMediaTypeNotSupported(ex, headers, status, request);

    String unsupportedMediaType = ObjectUtils.isEmpty(ex.getContentType()) ? "" :
        ex.getContentType().getType().concat("/").concat(ex.getContentType().getSubtype());

    this.response = ApiErrorResponse.builder(headers, status, UNSUPPORTED_MEDIA_TYPE)
        .message(UNSUPPORTED_MEDIA_TYPE.format(unsupportedMediaType))
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, UNSUPPORTED_MEDIA_TYPE)
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {

    ServletWebRequest servletWebRequest = ((ServletWebRequest) request);
    NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException(
        Objects.requireNonNull(servletWebRequest.getHttpMethod()).toString(),
        servletWebRequest.getRequest().getRequestURI(),
        headers);

    return this.handleNoHandlerFoundException(noHandlerFoundException, headers,
        HttpStatus.NOT_FOUND, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleMissingServletRequestParameter(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, BAD_REQUEST)
        .message("필수 입력 파라미터입니다.")
        .field(ex.getParameterName())
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
      ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleServletRequestBindingException(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, BAD_REQUEST)
        .message(ex.getMessage())
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleConversionNotSupported(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, INTERNAL_SERVER_ERROR)
        .message(ex.getMessage()).build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    super.handleTypeMismatch(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, BAD_REQUEST).build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleHttpMessageNotReadable(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, HTTP_MESSAGE_NOT_READABLE).build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleHttpMessageNotWritable(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, HTTP_MESSAGE_NOT_WRITABLE).build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleMethodArgumentNotValid(ex, headers, status, request);

    return this.handleBindException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleMissingServletRequestPart(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, BAD_REQUEST)
        .message("필수 입력 파라미터입니다.")
        .field(ex.getRequestPartName())
        .build();
    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    super.handleBindException(ex, headers, status, request);

    FieldError fieldError = Optional.ofNullable(ex.getFieldError()).orElseGet(() ->
    new FieldError("", "", ex.getMessage()));

    this.response = ApiErrorResponse.builder(headers, status, BAD_REQUEST)
            .message(fieldError.getDefaultMessage())
            .field(fieldError.getField())
            .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleNoHandlerFoundException(ex, headers, status, request);

    this.response = ApiErrorResponse.builder(headers, status, NOT_FOUND)
            .path(ex.getRequestURL())
            .build();
    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
      WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    String invalidValue = "";
    String message = "";
    for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
      invalidValue = constraintViolation.getPropertyPath().toString();
      message = constraintViolation.getMessage();
      break;
    }

    this.response =  ApiErrorResponse.builder(headers, status, BaseServiceStatus.BAD_REQUEST)
        .message(invalidValue)
        .field(message)
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @ExceptionHandler(value = ServiceException.class)
  protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
    HttpHeaders headers = ex.getHeaders();
    HttpStatus status = ex.getStatus();

    this.response = ApiErrorResponse.builder(headers, status, ex.getServiceStatus())
        .message(ex.getMessage())
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

  @ExceptionHandler(value = RuntimeException.class)
  protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex,
      WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    this.response = ApiErrorResponse.builder(headers, status, INTERNAL_SERVER_ERROR)
        .message(ex.getMessage())
        .build();

    return super.handleExceptionInternal(ex, this.response, headers, status, request);
  }

}
