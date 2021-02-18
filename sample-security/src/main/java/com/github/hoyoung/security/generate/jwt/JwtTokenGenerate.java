package com.github.hoyoung.security.generate.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

/**
 * Created by HoYoung on 2021/02/16.
 */
public class JwtTokenGenerate {

  public String create() {
    Algorithm algorithmHS = Algorithm.HMAC256("secret");
    return JWT.create()
        .withIssuer("auth0")
        .withExpiresAt(new Date())
        .withSubject("subject")
//        .withClaim("")
        .sign(algorithmHS)
        ;
  }


//  public static String create() {
//    return
//  }
}
