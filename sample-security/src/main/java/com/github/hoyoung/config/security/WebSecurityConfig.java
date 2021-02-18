package com.github.hoyoung.config.security;

import com.github.hoyoung.security.authentication.BasicAuthenticationEntryPoint;
import com.github.hoyoung.security.authentication.filter.BasicAuthenticationProcessingFilter;
import com.github.hoyoung.security.authentication.provider.BasicAuthenticationProvider;
import com.github.hoyoung.security.handler.BasicAccessDeniedHandler;
import com.github.hoyoung.security.handler.BasicAuthenticationFailureHandler;
import com.github.hoyoung.security.handler.BasicAuthenticationSuccessHandler;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by HoYoung on 2021/02/09.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  public static final String AUTHENTICATION_HEADER_NAME = "Authorization";
  public static final String AUTHENTICATION_URL = "/login";
  public static final String REFRESH_TOKEN_URL = "/api/auth/token";
  public static final String API_ROOT_URL = "/books/**";

  private final BasicAuthenticationEntryPoint basicAuthenticationEntryPoint;

  private final BasicAuthenticationSuccessHandler authenticationSuccessHandler; //인증성공시
  private final BasicAuthenticationFailureHandler authenticationFailureHandler; //인증실패시

  private final BasicAccessDeniedHandler basicAccessDeniedHandler;  //403 처리
  private final BasicAuthenticationProvider basicAuthenticationProvider; //401 처리

  protected AbstractAuthenticationProcessingFilter basicAuthenticationProcessingFilter()
      throws Exception {
    BasicAuthenticationProcessingFilter basicAuthenticationFilter =
        new BasicAuthenticationProcessingFilter(super.authenticationManager());
    basicAuthenticationFilter.setAuthenticationSuccessHandler(this.authenticationSuccessHandler);
    basicAuthenticationFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler);
    return basicAuthenticationFilter;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(this.basicAuthenticationProvider);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    List<String> permitAllEndpointList = Arrays.asList(
        AUTHENTICATION_URL,
        REFRESH_TOKEN_URL
    );

    http.authorizeRequests().anyRequest().authenticated().and()
        .formLogin().loginProcessingUrl("/processing/login").and()
        .logout().and()
        .csrf().disable()
        .exceptionHandling().and()
        .addFilterBefore(this.basicAuthenticationProcessingFilter(),
            UsernamePasswordAuthenticationFilter.class)
    ;

//    http
//        .csrf().disable()
////        .exceptionHandling()
////          .authenticationEntryPoint(this.customAuthenticationEntryPoint)
////          .accessDeniedHandler(this.asyncAccessDeniedHandler)
////        .and()
//          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        .formLogin()
//        .and()
//          .authorizeRequests()
//          .antMatchers(permitAllEndpointList.toArray(new String[permitAllEndpointList.size()]))
//          .permitAll()
//        .and()
//          .authorizeRequests()
//          .antMatchers(API_ROOT_URL).authenticated() // Protected API End-points
//        .and()
//        .logout()
//        .addFilterBefore(this.asyncAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)





    /*


    http
        .csrf().disable()
          .exceptionHandling().authenticationEntryPoint(this.customAuthenticationEntryPoint)
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests(a -> a.antMatchers("/login", "/error", "/webjars/**")
            .permitAll()
            .anyRequest()
            .authenticated())
        .authenticationProvider(this.customAuthenticationProvider)
//        .formLogin().and()
        .logout(l -> l.logoutSuccessUrl("/login").permitAll())


     */
//        .httpBasic().disable()
//        .csrf().disable()
//        .formLogin().disable()
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        .authorizeRequests()
//        .antMatchers(HttpMethod.GET, "/books/**").hasRole("USER")
//        .antMatchers(HttpMethod.POST, "/books").hasRole("ADMIN")
//        .antMatchers(HttpMethod.PUT, "/books/**").hasRole("ADMIN")
//        .antMatchers(HttpMethod.PATCH, "/books/**").hasRole("ADMIN")
//        .antMatchers(HttpMethod.DELETE, "/books/**").hasRole("ADMIN")
//        .and()
//        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
//        .and()
        ;

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//      http
//          .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
//          .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
//          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
//          .and()
//          .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
//          .antMatchers("/*/signin", "/*/signup").permitAll() // 가입 및 인증 주소는 누구나 접근가능
//          .antMatchers(HttpMethod.GET, "/exception/**", "helloworld/**").permitAll() // hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
//          .antMatchers("/*/users").hasRole("ADMIN")
//          .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
//          .and()
//          .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
//          .and()
//          .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//          .and()
//          .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt token 필터를 id/password 인증 필터 전에 넣어라.
//    }

  }

//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////    PasswordEncoder.encode(1234);
//    auth.inMemoryAuthentication()
//        .withUser("user").password(new BCryptPasswordEncoder().encode("1234")).roles("USER")
//        .and()
//        .withUser("admin").password(new BCryptPasswordEncoder().encode("1234")).roles("USER", "ADMIN");
//  }

}

//  @Bean
//  public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService(WebClient rest) {
//    DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//    return request -> {
//      OAuth2User user = delegate.loadUser(request);
//      if (!"github".equals(request.getClientRegistration().getRegistrationId())) {
//        return user;
//      }
//
//      OAuth2AuthorizedClient client = new OAuth2AuthorizedClient
//          (request.getClientRegistration(), user.getName(), request.getAccessToken());
//      String url = user.getAttribute("organizations_url");
//      List<Map<String, Object>> orgs = rest
//          .get().uri(url)
//          .attributes(oauth2AuthorizedClient(client))
//          .retrieve()
//          .bodyToMono(List.class)
//          .block();
//
//      if (orgs.stream().anyMatch(org -> "spring-projects".equals(org.get("login")))) {
//        return user;
//      }
//
//      throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Not in Spring Team", ""));
//    };
//  }