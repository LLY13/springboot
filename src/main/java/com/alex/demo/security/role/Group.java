package com.alex.demo.security.role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Role Group, keep immutable!
 */
@Getter
@ToString
@NoArgsConstructor
public class Group {

  private String id;
  private String name;
  private String urls;

}
