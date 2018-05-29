package com.alex.demo.constant;

import com.alex.demo.security.role.Rights;
import com.alex.demo.util.JsonUtil;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

public class Constant {

  // project default date time format
  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  public static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;


  public static final Rights RIGHTS;

  static {
    try {
      RIGHTS = JsonUtil.YML_MAPPER.readValue(
          Constant.class.getResourceAsStream("/rights.yml"), Rights.class);
      Assert.notEmpty(RIGHTS.getGroups(), "Empty Rights!");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
