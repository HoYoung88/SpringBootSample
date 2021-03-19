package com.github.hoyoung.web.status.service;

/**
 * Created by HoYoung on 2021/01/21.
 */
public interface BasicServiceStatus {
  String getCode();
  String getMessage();
  String name();

  public String format(Object... args);
}