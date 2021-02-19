package com.github.hoyoung.api.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HoYoung on 2021/02/19.
 */
@RestController
@RequestMapping(value = "/user/")
@Slf4j
public class UserController {
  @RequestMapping(value = "")
  public void user() {
    log.debug("{}", ">>>>>>>");
  }
}
