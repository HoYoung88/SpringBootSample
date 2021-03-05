package com.github.hoyoung.config.security;

import com.github.hoyoung.security.authentication.BasicAuthenticationEntryPoint;
import com.github.hoyoung.security.authentication.SkipPathRequestMatcher;
import com.github.hoyoung.security.authentication.filter.BasicAuthenticationProcessingFilter;
import com.github.hoyoung.security.authentication.filter.jwt.JwtAuthenticationProcessingFilter;
import com.github.hoyoung.security.authentication.provider.BasicAuthenticationProvider;
import com.github.hoyoung.security.authentication.provider.jwt.JwtTokenAuthenticationProvider;
import com.github.hoyoung.security.entity.Role;
import com.github.hoyoung.security.generate.jwt.JwtTokenGenerate;
import com.github.hoyoung.security.handler.BasicAccessDeniedHandler;
import com.github.hoyoung.security.handler.BasicAuthenticationResultHandler;
import com.github.hoyoung.security.user.service.BasicOAuth2AuthorizedClientService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by HoYoung on 2021/02/09.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String AUTHENTICATION_URL = "/login";
  public static final String REFRESH_TOKEN_URL = "/api/auth/token";
  public static final String API_ROOT_URL = "/users/**";

  @Autowired
  private AuthenticationManager authenticationManager;

  private final JwtTokenGenerate jwtTokenGenerate;
  private final BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;
  private final BasicAuthenticationResultHandler basicAuthenticationResultHandler; //인증 결과 처리
  private final BasicAccessDeniedHandler basicAccessDeniedHandler;  //403 처리
  private final BasicAuthenticationProvider basicAuthenticationProvider; //401 처리
  private final JwtTokenAuthenticationProvider jwtTokenAuthenticationProvider;

  protected AbstractAuthenticationProcessingFilter basicAuthenticationProcessingFilter()
      throws Exception {
    BasicAuthenticationProcessingFilter filter =
        new BasicAuthenticationProcessingFilter(authenticationManager);
    filter.setBasicAuthenticationResultHandler(this.basicAuthenticationResultHandler);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  protected AbstractAuthenticationProcessingFilter jwtAuthenticationProcessingFilter(
      List<String> pathsToSkip, String pattern) {
    SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, pattern);
    JwtAuthenticationProcessingFilter filter =
        new JwtAuthenticationProcessingFilter(matcher);
    filter.setJwtTokenGenerate(jwtTokenGenerate);
    filter.setBasicAuthenticationResultHandler(this.basicAuthenticationResultHandler);
    filter.setAuthenticationManager(this.authenticationManager);
    return filter;
  }

  @Bean
  public OAuth2AuthorizedClientService authorizedClientService() {
    return new BasicOAuth2AuthorizedClientService();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(this.basicAuthenticationProvider);
    auth.authenticationProvider(this.jwtTokenAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    List<String> permitAllEndpointList = Arrays.asList(
        AUTHENTICATION_URL,
        REFRESH_TOKEN_URL,
        BasicAuthenticationProcessingFilter.DEFAULT_ANT_PATH_REQUEST_MATCHER.getPattern()
    );

    http.authorizeRequests()
          .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
          .permitAll()
        .and()
          .authorizeRequests()
          .antMatchers("/users/me").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
          .antMatchers(API_ROOT_URL).hasAnyRole(Role.USER.name())
//          .antMatchers(API_ROOT_URL).authenticated()
        .and()
          .formLogin()
            .disable()
          .oauth2Login()
            .successHandler(this.basicAuthenticationResultHandler)
            .failureHandler(this.basicAuthenticationResultHandler)
        .and()
          .logout().logoutSuccessHandler(this.basicAuthenticationResultHandler).and()
          .headers().frameOptions().disable()
        .and()
          .csrf().disable().exceptionHandling()
            .authenticationEntryPoint(this.basicAuthenticationEntryPoint)
            .accessDeniedHandler(this.basicAccessDeniedHandler)
        .and()
          .httpBasic().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
          .addFilterBefore(this.basicAuthenticationProcessingFilter(),
              UsernamePasswordAuthenticationFilter.class)
          .addFilterBefore(this.jwtAuthenticationProcessingFilter(permitAllEndpointList, API_ROOT_URL),
              UsernamePasswordAuthenticationFilter.class)
    ;
  }
}
