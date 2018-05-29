package com.alex.demo.config;

import com.alex.demo.constant.Constant;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

@Configuration
public class DateTimeConfig {

//  @Bean
//  @Primary
//  public ObjectMapper serializingObjectMapper() {
//    return JsonUtil.MAPPER;
//  }

  @Bean
  public Formatter<LocalDate> localDateFormatter() {
    return new Formatter<LocalDate>() {
      @Override
      public LocalDate parse(String text, Locale locale) throws ParseException {
        return LocalDate.parse(text, Constant.DATE_FORMAT);
      }

      @Override
      public String print(LocalDate object, Locale locale) {
        return Constant.DATE_FORMAT.format(object);
      }
    };
  }

  @Bean
  public Formatter<LocalDateTime> localDateTimeFormatter() {
    return new Formatter<LocalDateTime>() {
      @Override
      public LocalDateTime parse(String text, Locale locale) throws ParseException {
        return LocalDateTime.parse(text, Constant.DATETIME_FORMAT);
      }

      @Override
      public String print(LocalDateTime object, Locale locale) {
        return Constant.DATETIME_FORMAT.format(object);
      }
    };
  }

}
