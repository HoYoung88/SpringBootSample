package com.github.hoyoung.api.product.model;

import com.github.hoyoung.api.product.entity.ProductOption;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by HoYoung on 2021/04/09.
 */
@Getter
@ToString
public class ProductOptionModel {
  private final Long productOptionId;
  private final String optionName;
  private final int quantity;

  @Builder
  public ProductOptionModel(Long productOptionId, String optionName, int quantity) {
    this.productOptionId = productOptionId;
    this.optionName = optionName;
    this.quantity = quantity;
  }

  public static ProductOptionModel of(ProductOption productOption) {
    return ProductOptionModel.builder()
        .productOptionId(productOption.getId())
        .optionName(productOption.getOptionName())
        .quantity(productOption.getQuantity())
        .build();
  }

  public static List<ProductOptionModel> of(List<ProductOption> productOptions) {
    return productOptions.stream().map(ProductOptionModel::of).collect(Collectors.toList());
  }
}
