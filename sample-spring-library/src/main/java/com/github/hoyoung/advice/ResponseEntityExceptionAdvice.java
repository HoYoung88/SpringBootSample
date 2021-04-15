package com.github.hoyoung.advice;


import com.github.hoyoung.model.response.ErrorResponse;
import java.util.Objects;
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
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Created by HoYoung on 2021/01/27.
 */
@Slf4j
@RestControllerAdvice
//@EnableWebMvc
public class ResponseEntityExceptionAdvice extends ResponseEntityExceptionHandler {

  public ResponseEntityExceptionAdvice() {
    super();
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.methodNotAllowed(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.unsupportedMediaType(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.unsupportedMediaType(ex.getMessage()),
        headers, status, request);
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
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
      ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    super.handleServletRequestBindingException(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleConversionNotSupported(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.internalServerError(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    super.handleTypeMismatch(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleHttpMessageNotReadable(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleHttpMessageNotWritable(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
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
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    super.handleBindException(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.badRequest(ex.getMessage()),
        headers, status, request);
  }

  @Override
  @ResponseStatus(value = HttpStatus.NOT_FOUND )
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    super.handleNoHandlerFoundException(ex, headers, status, request);
    return this.handleExceptionInternal(ex, ErrorResponse.notFound(ex.getMessage()),
        headers, status, request);
  }

  @ExceptionHandler(value = ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
      WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    String invalidValue = "";
    String message = "";
    for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
      invalidValue = getPropertyName(constraintViolation.getPropertyPath().toString());
      message = constraintViolation.getMessage();
      break;
    }

    return this.handleExceptionInternal(ex, ErrorResponse.badRequest("[" + invalidValue + "]" + message),
        headers, status, request);
  }

  @ExceptionHandler(value = {RuntimeException.class, Exception.class})
  protected ResponseEntity<Object> handleRuntimeException(Exception ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    return this.handleExceptionInternal(ex, ErrorResponse.internalServerError(ex.getMessage()),
        headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    ex.printStackTrace();
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  private String getPropertyName(String propertyPath) {
    return propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
  }
}
