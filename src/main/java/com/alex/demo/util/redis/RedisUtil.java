package com.alex.demo.util.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RedisUtil extends AbstractRedisUtil {

  @Autowired
  public void setTemplate(RedisTemplate<String, Object> template) {
    super.template = template;
  }


}
