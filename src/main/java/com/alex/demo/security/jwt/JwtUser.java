package com.alex.demo.security.jwt;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {

  private String id;
  private String phone;
  private String platform;
  private List<String> rights;
  private Date expire;

}
