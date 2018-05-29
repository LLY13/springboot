package com.alex.demo.constant;

public class DatabaseEnum {

  public enum Gender implements FirstChar {
    FEMALE, MALE
  }

  /**
   * 首字母
   */
  public interface FirstChar {

    String name();

    default char v() {
      return name().charAt(0);
    }
  }

  public interface Normal {

    String name();

    default String v() {
      return name();
    }
  }

  public interface Ordinal {

    int ordinal();

    default int v() {
      return ordinal();
    }
  }
}
