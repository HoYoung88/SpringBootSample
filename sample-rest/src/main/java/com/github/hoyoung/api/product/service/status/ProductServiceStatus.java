package com.github.hoyoung.api.product.service.status;

import lombok.Getter;

/**
 * Created by HoYoung on 2021/04/09.
 */
@Getter
public enum ProductServiceStatus {
  NOT_FOUND_PRODUCT("1000", "상품정보를 찾을 수 없습니다."),
  DUPLICATION_PRODUCT("1001", "이미 저장된 상품명입니다.")
  ;

  private final String code;
  private final String message;
  ProductServiceStatus(String code, String message) {
    this.code = code;
    this.message = message;
  }
}
