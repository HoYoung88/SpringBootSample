package com.github.hoyoung.api.product.advice;

import com.github.hoyoung.api.product.exception.ProductException;
import com.github.hoyoung.api.product.exception.ProductException.NotFoundProductException;
import com.github.hoyoung.api.product.exception.ProductException.ProductDuplicationException;
import com.github.hoyoung.api.product.service.status.ProductServiceStatus;
import com.github.hoyoung.model.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created by HoYoung on 2021/04/09.
 */
@RestControllerAdvice
public class ProductControllerAdvice {

  @ExceptionHandler(NotFoundProductException.class)
  public ResponseEntity<ErrorResponse> notFound(NotFoundProductException productException) {
    ProductServiceStatus productServiceStatus = productException.getProductServiceStatus();
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.builder()
            .error(productServiceStatus.getCode())
            .errorDesc(productServiceStatus.name())
            .message(productServiceStatus.getMessage())
            .build());
  }

  @ExceptionHandler({ProductDuplicationException.class})
  public ResponseEntity<ErrorResponse> badRequest(ProductException productException) {
    ProductServiceStatus productServiceStatus = productException.getProductServiceStatus();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.builder()
            .error(productServiceStatus.getCode())
            .errorDesc(productServiceStatus.name())
            .message(productServiceStatus.getMessage())
            .build());
  }

}
