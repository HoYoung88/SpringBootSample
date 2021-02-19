package com.github.hoyoung.security.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Component;

/**
 * Created by HoYoung on 2021/02/19.
 */
@Component
@Slf4j
public class BasicOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

  @Override
  public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId,
      String principalName) {
    log.warn("==== loadAuthorizedClient => {}, {}", clientRegistrationId, principalName);
    return null;
  }

  @Override
  public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient,
      Authentication principal) {
    log.warn("==== saveAuthorizedClient => {}, {}", authorizedClient.toString(), principal.toString());
  }

  @Override
  public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
    log.warn("==== removeAuthorizedClient => {}, {}", clientRegistrationId, principalName);
  }
}
