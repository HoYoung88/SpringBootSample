package com.github.hoyoung;

import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by HoYoung on 2021/02/17.
 */
@SpringBootApplication
public class SecurityApplication {
  @PostConstruct
  private void init() {
    Locale.setDefault(Locale.KOREA);
    TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
  }

  public static void main(String[] args) {
    SpringApplication.run(SecurityApplication.class, args);
  }
}
