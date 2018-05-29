package com.alex.demo.constant;

public final class RedisConst {
  private static final String CUSTOMER = "customer:";

  public static String RedisConst(String id) {
    return CUSTOMER + id;
  }
}
