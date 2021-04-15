package com.github.hoyoung.api.product.repository;

import com.github.hoyoung.api.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by HoYoung on 2021/04/08.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}
