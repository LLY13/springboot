package com.alex.demo.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class JwtAuthentication extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -5887874329573955959L;

  private UserDetails principal;
  private String token;

  public JwtAuthentication(UserDetails principal, String jsonWebToken) {
    super(principal.getAuthorities());
    this.principal = principal;
    this.token = jsonWebToken;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
