package com.alex.demo.util.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringRedisUtil extends AbstractRedisUtil {

  @Autowired
  public void setTemplate(StringRedisTemplate template) {
    super.template = template;
  }
}
