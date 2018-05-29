package com.alex.demo.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.InputStream;

/**
 * Jackson 工具包
 */
public class JsonUtil {


  public static ObjectMapper MAPPER = new ObjectMapper();

  public static ObjectMapper YML_MAPPER = new ObjectMapper(new YAMLFactory());
//
//  public static ObjectMapper XML_MAPPER = new XmlMapper();

//  static {
//    // 不序列化null字段
////    MAPPER.setSerializationInclusion(Include.NON_NULL);
//
//    // 解序列化时不处理未知字段
//    MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//
//    JavaTimeModule module = new JavaTimeModule();
//    module.addSerializer(LocalDate.class, new LocalDateSerializer(Constant.DATE_FORMAT));
//    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(Constant.DATE_FORMAT));
//    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(Constant.DATETIME_FORMAT));
//    module.addDeserializer(LocalDateTime.class,
//        new LocalDateTimeDeserializer(Constant.DATETIME_FORMAT));
//    MAPPER.registerModule(module);
//
//    // 仅处理成员变量
//    MAPPER.setVisibility(MAPPER.getSerializationConfig().getDefaultVisibilityChecker()
//        .withFieldVisibility(Visibility.ANY).withGetterVisibility(Visibility.NONE)
//        .withSetterVisibility(Visibility.NONE).withCreatorVisibility(Visibility.NONE)
//        .withIsGetterVisibility(Visibility.NONE));
//  }

  public static <T> T read(String str, Class<T> clz) {
    try {
      return MAPPER.readValue(str, clz);
    } catch (Exception e) {
      e.printStackTrace();
//      throw Exception.of(ResponseEnum.JSON_ERROR).with(e);
      return null;
    }
  }

  public static <T> T read(InputStream in, Class<T> clz) {
    try {
      return MAPPER.readValue(in, clz);
    } catch (Exception e) {
      e.printStackTrace();
//      throw Exception.of(ResponseEnum.JSON_ERROR).with(e);
      return null;
    }
  }

  public static String write(Object obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (Exception e) {
      e.printStackTrace();
//      throw Exception.of(ResponseEnum.JSON_ERROR).with(e);
      return null;
    }
  }

  public static JsonNode readTree(String jsonStr) {
    try {
      return MAPPER.readTree(jsonStr);
    } catch (Exception e) {
//      throw Exception.of(ResponseEnum.JSON_ERROR).with(e);
      return null;
    }
  }

}
