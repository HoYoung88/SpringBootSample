package com.github.hoyoung.api.product.exception;

import com.github.hoyoung.api.product.service.status.ProductServiceStatus;
import lombok.Getter;

/**
 * Created by HoYoung on 2021/04/09.
 */
@Getter
public class ProductException extends RuntimeException {
  private final ProductServiceStatus productServiceStatus;

  public ProductException(ProductServiceStatus productServiceStatus) {
    super(productServiceStatus.getMessage());
    this.productServiceStatus = productServiceStatus;
  }

  @Getter
  public static class NotFoundProductException extends ProductException {
    private final ProductServiceStatus productServiceStatus;
    public NotFoundProductException() {
      super(ProductServiceStatus.NOT_FOUND_PRODUCT);
      this.productServiceStatus = ProductServiceStatus.NOT_FOUND_PRODUCT;
    }
  }

  @Getter
  public static class ProductDuplicationException extends ProductException {
    private final ProductServiceStatus productServiceStatus;
    public ProductDuplicationException() {
      super(ProductServiceStatus.DUPLICATION_PRODUCT);
      this.productServiceStatus = ProductServiceStatus.DUPLICATION_PRODUCT;
    }
  }

}
