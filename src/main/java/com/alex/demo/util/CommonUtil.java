package com.alex.demo.util;

import java.util.UUID;

public class CommonUtil {
  /**
   * get uuid without dashes
   *
   * @return uuid
   */
  public static String uuid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }


  public static <T> T newInstance(Class<T> type) {
    try {
      return type.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

}
