package com.alex.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class CustomConfig {

  @Value("${name.lastname}")
  private String lastname;

  @Value("${name.firstname.english}")
  private String english;

  @Value("${name.firstname.chinese}")
  private String chinese;

  @Value("${myenglishname}")
  private String fullname;

}
