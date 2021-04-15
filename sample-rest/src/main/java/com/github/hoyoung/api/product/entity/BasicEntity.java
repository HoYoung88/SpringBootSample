package com.github.hoyoung.api.product.entity;

/**
 * Created by HoYoung on 2021/03/12.
 */

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class BasicEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "create_date", updatable = false)
  private final LocalDateTime createDate = LocalDateTime.now();

  @Column(name = "update_date", insertable = false)
  private final LocalDateTime updateDate = LocalDateTime.now();

}
