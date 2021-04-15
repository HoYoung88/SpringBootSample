package com.github.hoyoung.api.product.repository;

import com.github.hoyoung.api.product.entity.Product;
import com.github.hoyoung.api.product.entity.ProductOption;
import com.github.hoyoung.api.product.entity.QProduct;
import com.github.hoyoung.api.product.entity.QProductOption;
import com.github.hoyoung.api.product.model.ProductModel;
import com.github.hoyoung.api.product.model.ProductOptionModel;
import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * Created by HoYoung on 2021/04/09.
 */
@Repository
@RequiredArgsConstructor
public class SimpleProductRepository {
  private final JPAQueryFactory query;
  private final ProductRepository productRepository;
  private final QProduct qProduct = QProduct.product;
  private final QProductOption qProductOption = QProductOption.productOption;

  public Map<Product, List<ProductOption>> products() {
    return query.select(qProduct)
        .from(qProduct, qProductOption)
        .where(qProduct.id.eq(qProductOption.product.id))
        .offset(1)
        .limit(5)
        .transform(GroupBy.groupBy(qProduct).as(GroupBy.list(qProductOption)));
  }

  public Optional<Product> findByProductId(Long productId) {
    return Optional.ofNullable(query.selectFrom(qProduct)
        .where(qProduct.id.eq(productId))
        .fetchOne());
  }

  public Optional<Product> findByProductName(ProductModel productModel) {
    return Optional.ofNullable(query.selectFrom(qProduct)
        .where(qProduct.name.eq(productModel.getName()))
        .fetchOne());
  }

  public Product saveProduct(ProductModel productModel) {
    Product prodcut = Product.builder().name(productModel.getName())
        .displayStartDate(productModel.getDisplayStartDate())
        .displayEndDate(productModel.getDisplayEndDate())
        .price(productModel.getPrice())
        .stock(productModel.getStock())
        .build();

    for(ProductOptionModel productOptionModel : productModel.getOptions()) {
      prodcut.addOption(ProductOption.builder()
          .optionName(productOptionModel.getOptionName())
          .quantity(productOptionModel.getQuantity())
          .build());
    }

    return productRepository.save(prodcut);
  }

  public void deleteProduct(Long productId) {
    this.query.delete(qProductOption)
        .where(qProductOption.product.id.eq(productId))
        .execute();

    this.query.delete(qProduct)
        .where(qProduct.id.eq(productId))
        .execute();
  }
}
