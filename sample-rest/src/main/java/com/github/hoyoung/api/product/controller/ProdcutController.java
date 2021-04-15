package com.github.hoyoung.api.product.controller;

import com.github.hoyoung.api.product.exception.ProductException;
import com.github.hoyoung.api.product.model.ProductModel;
import com.github.hoyoung.api.product.service.ProductService;
import com.github.hoyoung.model.response.SuccessResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by HoYoung on 2021/04/08.
 */
@RestController
@RequestMapping(value = "/products")
@RequiredArgsConstructor
@Slf4j
public class ProdcutController {
  private final ProductService productService;

  @GetMapping("")
  public SuccessResponse<List<ProductModel>> products() {
    return SuccessResponse.withData(productService.products());
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductModel> products(@PathVariable("productId") Long productId) {
    return ResponseEntity.ok(this.productService.product(productId));
  }

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SuccessResponse<ProductModel>> addProduct(
      @RequestBody ProductModel productModel) throws URISyntaxException {
    UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();
    productModel = this.productService.saveProduct(productModel);

    URI createUrl = new URI(Objects.requireNonNull(builder.buildAndExpand().getPath())
        .concat("/")
        .concat(productModel.getId().toString()));

    return ResponseEntity.created(createUrl)
        .body(SuccessResponse.withData(productModel));
  }

  @PutMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> editProduct(@PathVariable("productId") Long productId,
      @RequestBody ProductModel productModel) {
    try {
      productModel.addProductId(productId);
      this.productService.updateProduct(productModel);
      return ResponseEntity.ok("");
    } catch (ProductException p) {
      return ResponseEntity.noContent().build();
    }
  }

  @PutMapping(value = "/{productId}/options/{productOptionId}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> editProductOption(@PathVariable("productId") Long productId,
      @PathVariable("productOptionId") Long productOptionId) {
    this.productService.updateProductOption(productId, productOptionId);
    return ResponseEntity.ok("");
  }

  @DeleteMapping(value = "/{productId}")
  public ResponseEntity<Object> deleteProduct(@PathVariable("productId") Long productId) {
    try {
      this.productService.deleteProduct(productId);
      return ResponseEntity.ok("");
    } catch (ProductException p) {
      return ResponseEntity.noContent().build();
    }
  }
}
