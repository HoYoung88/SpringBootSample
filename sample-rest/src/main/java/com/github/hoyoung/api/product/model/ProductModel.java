package com.github.hoyoung.api.product.model;

import com.github.hoyoung.api.product.entity.Product;
import com.github.hoyoung.api.product.entity.ProductOption;
import com.github.hoyoung.api.product.entity.StockStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by HoYoung on 2021/04/08.
 */
@Getter
@ToString
public class ProductModel {
  private Long id;
  private final String name;
  private final long price;
  private final StockStatus stock;
  private final LocalDateTime displayStartDate;
  private final LocalDateTime displayEndDate;
  private final List<ProductOptionModel> options;

  @Builder
  public ProductModel(Long id, String name, long price, StockStatus stock,
      LocalDateTime displayStartDate,
      LocalDateTime displayEndDate,
      List<ProductOptionModel> options) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.displayStartDate = displayStartDate;
    this.displayEndDate = displayEndDate;
    this.options = options;
  }

  public void addProductId(Long productId) {
    this.id = productId;
  }

  public static ProductModel of(Product product) {
    return ProductModel.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .displayStartDate(product.getDisplayStartDate())
        .displayEndDate(product.getDisplayEndDate())
        .stock(product.getStock())
        .options(ProductOptionModel.of(product.getOptions()))
        .build();
  }

  public static ProductModel of(Product product, List<ProductOption> options) {
    return ProductModel.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .stock(product.getStock())
        .displayStartDate(product.getDisplayStartDate())
        .displayEndDate(product.getDisplayEndDate())
        .options(ProductOptionModel.of(options))
        .build();
  }

  public static List<ProductModel> of(List<Product> products) {
    return products.stream().map(ProductModel::of).collect(Collectors.toList());
  }
}
