package com.github.hoyoung.config.security;

import com.github.hoyoung.security.authentication.provider.oauth2.KakaoOAuth2Provider;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * Created by HoYoung on 2021/02/19.
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository(
      OAuth2ClientProperties oAuth2ClientProperties) {
    List<ClientRegistration> registrations = new ArrayList<>();
    registrations.add(this.kakao(oAuth2ClientProperties));
    registrations.add(this.github(oAuth2ClientProperties));
//    registrations.add(this.google(oAuth2ClientProperties));
//    registrations.add(this.facebook(oAuth2ClientProperties));

    return new InMemoryClientRegistrationRepository(registrations);
  }

  public ClientRegistration kakao(OAuth2ClientProperties oAuth2ClientProperties) {
    String registrationId = "kakao";
    return KakaoOAuth2Provider.KAKAO.getBuilder(registrationId)
        .clientId(oAuth2ClientProperties.getRegistration().get(registrationId).getClientId())
        .build();
  }

  public ClientRegistration github(OAuth2ClientProperties oAuth2ClientProperties) {
    String registrationId = "github";
    return CommonOAuth2Provider.GITHUB.getBuilder(registrationId)
        .clientId(oAuth2ClientProperties.getRegistration().get(registrationId).getClientId())
        .clientSecret(oAuth2ClientProperties.getRegistration().get(registrationId).getClientSecret())
        .build();
  }

  public ClientRegistration google(OAuth2ClientProperties oAuth2ClientProperties) {
    String registrationId = "google";
    return CommonOAuth2Provider.GOOGLE.getBuilder(registrationId)
        .clientId(oAuth2ClientProperties.getRegistration().get(registrationId).getClientId())
        .clientSecret(oAuth2ClientProperties.getRegistration().get(registrationId).getClientSecret())
        .build();
  }

  public ClientRegistration facebook(OAuth2ClientProperties oAuth2ClientProperties) {
    String registrationId = "facebook";
    return CommonOAuth2Provider.FACEBOOK.getBuilder(registrationId)
        .clientId(oAuth2ClientProperties.getRegistration().get(registrationId).getClientId())
        .clientSecret(oAuth2ClientProperties.getRegistration().get(registrationId).getClientSecret())
        .build();
  }

}
