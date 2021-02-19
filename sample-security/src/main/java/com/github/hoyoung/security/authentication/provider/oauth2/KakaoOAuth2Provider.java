package com.github.hoyoung.security.authentication.provider.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * Created by HoYoung on 2021/02/19.
 * {@link org.springframework.security.config.oauth2.client.CommonOAuth2Provider} 참조
 */
public enum KakaoOAuth2Provider {
  KAKAO {

    @Override
    public Builder getBuilder(String registrationId) {
      ClientRegistration.Builder builder = getBuilder(registrationId,
          ClientAuthenticationMethod.POST);
      builder.scope("profile", "account_email");
      builder.authorizationUri("https://kauth.kakao.com/oauth/authorize");
      builder.tokenUri("https://kauth.kakao.com/oauth/token");
      builder.userInfoUri("https://kapi.kakao.com/v2/user/me");
      builder.userNameAttributeName("id");
      builder.clientName("Kakao");
      return builder;
    }
  };

  private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

  protected final ClientRegistration.Builder getBuilder(String registrationId,
      ClientAuthenticationMethod method) {
    ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
    builder.clientAuthenticationMethod(method);
    builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
    builder.redirectUri(DEFAULT_REDIRECT_URL);
    return builder;
  }

  public abstract ClientRegistration.Builder getBuilder(String registrationId);
}
