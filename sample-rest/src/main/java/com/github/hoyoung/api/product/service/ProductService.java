package com.github.hoyoung.api.product.service;

import com.github.hoyoung.api.product.entity.Product;
import com.github.hoyoung.api.product.exception.ProductException;
import com.github.hoyoung.api.product.exception.ProductException.NotFoundProductException;
import com.github.hoyoung.api.product.exception.ProductException.ProductDuplicationException;
import com.github.hoyoung.api.product.model.ProductModel;
import com.github.hoyoung.api.product.model.ProductOptionModel;
import com.github.hoyoung.api.product.repository.ProductRepository;
import com.github.hoyoung.api.product.repository.SimpleProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by HoYoung on 2021/04/08.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepository;
  private final SimpleProductRepository simpleProductRepository;

  @Transactional(readOnly = true)
  public List<ProductModel> products() {
    return this.simpleProductRepository.products().entrySet().stream().map(item -> {
      Product product = item.getKey();
      return ProductModel.builder()
          .id(product.getId())
          .name(product.getName())
          .price(product.getPrice())
          .stock(product.getStock())
          .options(ProductOptionModel.of(item.getValue()))
          .build();
    }).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ProductModel product(Long productId) {
    Product product = this.productRepository.findById(productId)
        .orElseThrow(NotFoundProductException::new);
    return ProductModel.of(product);
  }

  @Transactional
  public ProductModel saveProduct(ProductModel productModel) {
    Optional<Product> productOptional = this.simpleProductRepository.findByProductName(productModel);
    if(!productOptional.isPresent()) {
      Product product = this.simpleProductRepository.saveProduct(productModel);
      return ProductModel.of(product);
    } else {
      throw new ProductDuplicationException();
    }
  }

  @Transactional
  public void updateProduct(ProductModel productModel) throws ProductException {
    Product product = this.simpleProductRepository.findByProductId(productModel.getId())
        .orElseThrow(NotFoundProductException::new);
  }

  @Transactional
  public void updateProductOption(Long productId, Long productOptionId) throws ProductException {
    Product product = this.simpleProductRepository.findByProductId(productId)
        .orElseThrow(NotFoundProductException::new);

    product.getOptions().stream()
        .filter(item -> item.getId() == productOptionId)
        .findFirst()
        .orElseThrow(ProductDuplicationException::new)
        .changeOptionName(">>>>>><>>>");

    this.productRepository.save(product);

  }

  @Transactional
  public void deleteProduct(Long productId) throws ProductException {
    Product product = this.simpleProductRepository.findByProductId(productId)
        .orElseThrow(NotFoundProductException::new);
    this.simpleProductRepository.deleteProduct(productId);
  }
}
