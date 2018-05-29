package com.alex.demo.util.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

public abstract class AbstractRedisUtil {

  protected RedisTemplate template;

  public Boolean delete(String key) {
    return template.delete(key);
  }

  public Long delete(Collection collection) {
    return template.delete(collection);
  }

  public boolean hasKey(String key) {
    return template.hasKey(key);
  }

  public Set<String> keys(String pattern) {
    return template.keys(pattern);
  }

  public <T> T get(String key) {
    return (T) template.opsForValue().get(key);
  }

  public void increment(String key, long delta) {
    template.opsForValue().increment(key, delta);
  }

  public <T> void set(String key, T value) {
    template.opsForValue().set(key, value);
  }

  public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
    template.opsForValue().set(key, value, timeout, timeUnit);
  }

  /**
   * set map value with timeout <p> redis server version MUST BE higher than @2.6.0
   *
   * @param map String map
   */
  public void set(Map<String, String> map, long timeout, TimeUnit timeUnit) {
    long milliseconds = TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
    template.executePipelined(
        new RedisCallback<Object>() {
          @Nullable
          @Override
          public Object doInRedis(RedisConnection connection) throws DataAccessException {
            StringRedisConnection con = (StringRedisConnection) connection;
            map.forEach((k, v) -> con.pSetEx(k, milliseconds, v));
            return null;
          }
        }
    );
  }

  public <K, V> Map<K, V> hentries(String key) {
    return template.<K, V>opsForHash().entries(key);
  }

  public <K, V> void hput(String key, K hashKey, V value) {
    template.opsForHash().put(key, hashKey, value);
  }

  public <K, V> void hputAll(String key, Map<K, V> map) {
    template.opsForHash().putAll(key, map);
  }

  public <K, V> V hget(String key, K hashKey) {
    return (V) template.<K, V>opsForHash().get(key, hashKey);
  }

  public <K> Long hdelete(String key, K... hashKeys) {
    return template.opsForHash().delete(key, hashKeys);
  }

  public <K> void hincrement(String key, K hashKey, long delta) {
    template.opsForHash().increment(key, hashKey, delta);
  }


  public <T> Long sadd(String key, T... values) {
    return template.opsForSet().add(key, values);
  }

  public <T> Set<T> smembers(String key) {
    return (Set<T>) template.opsForSet().members(key);
  }

  public <T> Boolean sisMember(String key, T value) {
    return template.opsForSet().isMember(key, value);
  }

  public <T> T spop(String key) {
    return (T) template.opsForSet().pop(key);
  }

  public <T> List<T> spop(String key, long count) {
    return (List<T>) template.opsForSet().pop(key, count);
  }

  public Long ssize(String key) {
    return template.opsForSet().size(key);
  }

  public <T> Long sremove(String key, T... values) {
    return template.opsForSet().remove(key, values);
  }

}
