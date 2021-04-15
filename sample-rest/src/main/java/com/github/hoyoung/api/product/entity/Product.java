package com.github.hoyoung.api.product.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by HoYoung on 2021/03/12.
 */
@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BasicEntity {

  @Column(columnDefinition = "varchar(100) NOT NULL")
  private String name;

  @Column(columnDefinition = "int")
  private long price;

  @Enumerated(EnumType.STRING)
  @Column(name = "stock_yn", columnDefinition = "char(1) NOT NULL")
  private StockStatus stock = StockStatus.N;

  @Column(name = "display_start_date", columnDefinition = "datetime")
  private LocalDateTime displayStartDate;

  @Column(name = "display_end_date", columnDefinition = "datetime")
  private LocalDateTime displayEndDate;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private final List<ProductOption> options = new ArrayList<>();

  @Builder
  public Product(String name, long price, StockStatus stock, LocalDateTime displayStartDate,
      LocalDateTime displayEndDate) {
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.displayStartDate = displayStartDate;
    this.displayEndDate = displayEndDate;
  }

  public List<ProductOption> getOptions() {
    return Collections.unmodifiableList(options);
  }

  public void addOption(ProductOption productOption) {
    this.options.add(productOption);
    productOption.addProduct(this);
  }

  public void changeStockStatus() {
    if(this.stock.equals(StockStatus.Y)) {
      this.stock = StockStatus.N;
    } else {
      this.stock = StockStatus.Y;
    }
  }

  public void changeProductName(String productName) {
    this.name = productName;
  }

  public static ProductBuilder withName(String name) {
    return new ProductBuilder().name(name);
  }

}
