package com.alex.demo.security;

import com.alex.demo.constant.Constant;
import com.google.common.collect.Lists;
import com.alex.demo.security.role.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class MetadataSource implements FilterInvocationSecurityMetadataSource {

  private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

  private Collection<ConfigAttribute> loadResourceDefine() {
    // currently not allow dynamic configuration
    if (resourceMap == null) {
      resourceMap = new HashMap<>();
      List<Group> groups = Constant.RIGHTS.getGroups();
      if (groups == null) {
        return new ArrayList<>();
      }

      for (Group group : groups) {
        ConfigAttribute ca = new SecurityConfig(group.getId());
        List<String> resources = Lists.newArrayList(group.getUrls().split(","));
        for (String url : resources) {
          if (resourceMap.containsKey(url)) {
            Collection<ConfigAttribute> value = resourceMap.get(url);
            value.add(ca);
            resourceMap.put(url, value);
          } else {
            Collection<ConfigAttribute> atts = new ArrayList<>();
            atts.add(ca);
            resourceMap.put(url, atts);
          }
        }
      }
    }
    return resourceMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return loadResourceDefine();
  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    HttpServletRequest req = ((FilterInvocation) object).getHttpRequest();
    for (String url : resourceMap.keySet()) {
      RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
      if (requestMatcher.matches(req)) {
        return resourceMap.get(url);
      }
    }
    return null;
  }

  @Override
  public boolean supports(Class<?> arg0) {
    return true;
  }

}
