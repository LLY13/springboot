package com.alex.demo.response;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ResponseMsg {

  public static final String BAD_REQUEST = "明显的客户端错误,传参错误";
  public static final String UNAUTHORIZED = "用户没有必要的凭据,身份无法验证";
  public static final String FORBIDDEN = "无权操作";
  public static final String NOT_FOUND = "请求资源未找到";
  public static final String TOO_MANY_REQUESTS = "给定的时间内发送了太多的请求";

  public static final String INTERNAL_SERVER_ERROR = "服务器内部错误";
  public static final String NULL_POINTER_EXCEPTION = "服务器空指针异常";
  public static final String DB_EXCEPTION = "数据库异常,某些请求内容可能过时,检查传参是否正确";
  public static final String DB_JUNK_DATA = "数据库脏数据!";
  public static final String JSON_ERROR = "Json解析失败";
  public static final String UPDATE_FAIL = "数据更新失败";
  public static final String UPLOAD_FAIL = "上传失败";


}
