package com.alex.demo.security;

import java.util.Collection;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class DecisionManager implements AccessDecisionManager {

  @Override
  public void decide(Authentication authentication, Object object,
      Collection<ConfigAttribute> configAttributes)
      throws AccessDeniedException, InsufficientAuthenticationException {
    if (configAttributes == null) {
      return;
    }
    for (ConfigAttribute ca : configAttributes) {
      String needRole;
      if ((needRole = ca.getAttribute()) != null) {
        needRole = needRole.trim();
        for (GrantedAuthority ga : authentication.getAuthorities()) {
          if (needRole.equals(ga.getAuthority().trim())) {
            return;
          }
        }
      }
    }
//    throw new AccessDeniedException(WebSecurity.UNAUTHORIZED);
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

}
