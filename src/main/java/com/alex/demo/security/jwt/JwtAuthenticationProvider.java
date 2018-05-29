package com.alex.demo.security.jwt;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//    Authentication authenticatedUser;
    Authentication authenticatedUser = null;
    if (authentication.getClass().isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
        && authentication.getPrincipal() != null) {
      String tokenHeader = (String) authentication.getPrincipal();
      UserDetails userDetails = parseToken(tokenHeader);
      if (userDetails != null) {
//        if (userDetails.getUsername() != null && BlackHouse.exist(userDetails.getUsername())) {
//          throw new AccountExpiredException(ResponseMsg.UNAUTHORIZED + "- Account Expired");
//        }
        authenticatedUser = new JwtAuthentication(userDetails, tokenHeader);
      } else {
//        throw new BadCredentialsException(ResponseMsg.UNAUTHORIZED + "- Bad Credentials");
      }
    } else {
      authenticatedUser = authentication;
    }
    return authenticatedUser;
  }

  private UserDetails parseToken(String tokenHeader) {
    UserDetails principal = null;
    JwtUser detail = JwtUtil.parse(tokenHeader);

    if (detail != null) {
      // sso
//      if (!SingleSignOn.check(detail.getId(), detail.getExpire())) {
//        throw new CredentialsExpiredException(ResponseMsg.SINGLE_SIGN_ON);
//      }

      List<GrantedAuthority> authorities = detail.getRights().stream()
          .filter(e -> e != null && !e.isEmpty())
          .map(e -> new SimpleGrantedAuthority(e)).collect(Collectors.toList());
      principal = new User(detail.getId(), "", authorities);
    }

    return principal;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.isAssignableFrom(PreAuthenticatedAuthenticationToken.class)
        || authentication.isAssignableFrom(JwtAuthentication.class);
  }

}
