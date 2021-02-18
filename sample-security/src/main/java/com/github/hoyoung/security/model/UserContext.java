package com.github.hoyoung.security.model;

import com.github.hoyoung.security.entity.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by HoYoung on 2021/02/16.
 */
public class UserContext implements UserDetails {
  private final String username;
  private String password;
  private final boolean enabled;
  private final boolean accountNonExpired;
  private final boolean credentialsNonExpired;
  private final boolean accountNonLocked;
  private final List<GrantedAuthority> authorities;

  public UserContext(String username, String password, List<GrantedAuthority> authorities) {
    this(username, password, true, true,
        true, true, authorities);
  }

  public UserContext(String username, String password, boolean enabled, boolean accountNonExpired,
      boolean credentialsNonExpired, boolean accountNonLocked,
      List<GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
    this.authorities = Collections.unmodifiableList(authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return this.accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return this.credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  public static UserContextBuilder withUsername(String username) {
    return builder().username(username);
  }

  public static UserContextBuilder builder() {
    return new UserContextBuilder();
  }

  public static final class UserContextBuilder {
    private String username;
    private String password;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private boolean disabled;
    private List<GrantedAuthority> authorities;

    private UserContextBuilder () {}

    public UserContextBuilder username(String username){
      this.username = username;
      return this;
    }

    public UserContextBuilder password(String password){
      this.password = password;
      return this;
    }

    /**
     * 계정이 만료되었는지 여부를 정의합니다. 기본값은 false입니다.
     * @param accountExpired 계정이 만료된 경우 true이고, 그렇지 않으면 false입니다.
     * @return {@link UserContextBuilder} for method chaining
     */
    public UserContextBuilder accountExpired(boolean accountExpired){
      this.accountExpired = accountExpired;
      return this;
    }

    /**
     * 계정이 잠겨 있는지 여부를 정의합니다. 기본값은 false입니다.
     * @param accountLocked 계정이 잠긴 경우 true이고, 그렇지 않으면 false입니다.
     * @return {@link UserContextBuilder} for method chaining
     */
    public UserContextBuilder accountLocked(boolean accountLocked){
      this.accountLocked = accountLocked;
      return this;
    }

    /**
     * 자격 증명이 만료되었는지 여부를 정의합니다. 기본값은 false입니다.
     * @param credentialsExpired 자격 증명이 만료된 경우 true이고, 그렇지 않으면 false입니다.
     * @return {@link UserContextBuilder} for method chaining
     */
    public UserContextBuilder credentialsExpired(boolean credentialsExpired) {
      this.credentialsExpired = credentialsExpired;
      return this;
    }

    /**
     * 계정이 비활성화되었는지 여부를 정의합니다. 기본값은 false입니다.
     * @param disabled 계정이 비활성화된 경우 true이고, 그렇지 않으면 false입니다.
     * @return {@link UserContextBuilder} for method chaining
     */
    public UserContextBuilder disabled(boolean disabled){
      this.disabled = disabled;
      return this;
    }

    public UserContextBuilder roles(Role... roles) {
      List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
      for (Role role : roles) {
        authorities.add(new SimpleGrantedAuthority(role.authority()));
      }
      return authorities(authorities);
    }

    public UserContextBuilder authorities(GrantedAuthority... authorities) {
      return authorities(Arrays.asList(authorities));
    }

    public UserContextBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
      this.authorities = new ArrayList<>(authorities);
      return this;
    }

    public UserContext build() {
      return new UserContext(this.username, this.password, !this.disabled, !this.accountExpired,
          !this.credentialsExpired, !this.accountLocked, this.authorities);
    }

  }

}
