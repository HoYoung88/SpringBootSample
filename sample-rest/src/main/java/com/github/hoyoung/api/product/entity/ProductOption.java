package com.github.hoyoung.api.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by HoYoung on 2021/03/16.
 */
@Entity
@Table(name = "product_option")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption extends BasicEntity {

  @Column(name = "option_name", columnDefinition = "varchar(100) NOT NULL")
  private String optionName;

  @Column(name = "quantity", columnDefinition = "int")
  private int quantity;

  @ManyToOne()
  @JoinColumn(name = "product_id")
  private Product product;

  @Builder
  public ProductOption(String optionName, int quantity) {
    this.optionName = optionName;
    this.quantity = quantity;
  }

  public void addProduct(Product product) {
    this.product = product;
  }

  public void changeOptionName(String optionName) {
    this.optionName = optionName;
  }

}
