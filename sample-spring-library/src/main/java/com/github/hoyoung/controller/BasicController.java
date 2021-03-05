package com.github.hoyoung.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Created by HoYoung on 2021/02/26.
 */
@Slf4j
public class BasicController {

  @ModelAttribute
  private void modelAttribute() {
    log.debug(">>> ");
  }
}
