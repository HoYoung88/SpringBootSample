package com.github.hoyoung.api.user.controller;

import com.github.hoyoung.controller.BasicController;
import com.github.hoyoung.security.authentication.token.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HoYoung on 2021/02/19.
 */
@RestController
@RequestMapping(value = "/users")
@Slf4j
public class UserController extends BasicController {

  @GetMapping(value = "/me")
  public void userMe(JwtAuthenticationToken jwtAuthenticationToken) {
    log.debug(">>> login user info {}", jwtAuthenticationToken.toString());
  }
}
