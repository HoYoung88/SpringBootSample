package com.github.hoyoung.security.valid;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by HoYoung on 2021/02/16.
 */
public class ValidUserDetailsAuthentication {
  protected static void valid(UserDetails userDetails) {

    if(userDetails.isEnabled()) {

    }

  }
}
