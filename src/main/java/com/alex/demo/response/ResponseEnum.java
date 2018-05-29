package com.alex.demo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;

/**
 * 无数据成功返回,异常返回 <p>状态码参考 {@link org.springframework.http.HttpStatus}
 */
@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ResponseEnum implements Response {

  // success
  SUCCESS(200, 200, "OK"),

  // client error
  BAD_REQUEST(400, 400, ResponseMsg.BAD_REQUEST), // 明显的客户端错误,传参错误
  UNAUTHORIZED(401, 401, ResponseMsg.UNAUTHORIZED), // 用户没有必要的凭据,身份无法验证
  FORBIDDEN(403, 403, ResponseMsg.FORBIDDEN), // 无权操作
  NOT_FOUND(404, 404, ResponseMsg.NOT_FOUND), // 请求资源未找到
  TOO_MANY_REQUESTS(429, 429, ResponseMsg.TOO_MANY_REQUESTS), // 给定的时间内发送了太多的请求

  // server error
  INTERNAL_SERVER_ERROR(500, 500, ResponseMsg.INTERNAL_SERVER_ERROR), // 服务器内部错误

  NULL_POINTER_EXCEPTION(500, 1, ResponseMsg.NULL_POINTER_EXCEPTION), // 服务器空指针异常
  DB_EXCEPTION(500, 2, ResponseMsg.DB_EXCEPTION), // 数据库异常,某些请求内容可能过时,检查传参是否正确
  DB_JUNK_DATA(500, 3, ResponseMsg.DB_JUNK_DATA), // 数据库脏数据!
  JSON_ERROR(500, 4, ResponseMsg.JSON_ERROR), // Json解析失败
  UPDATE_FAIL(500, 5, ResponseMsg.UPDATE_FAIL), // 数据更新失败
  UPLOAD_FAIL(500, 6, ResponseMsg.UPLOAD_FAIL), // 上传失败

  // 末尾分号
  ;

  /**
   * http status code
   */
  private int status;
  /**
   * response id
   */
  private int code;
  /**
   * debug message for developers
   */
  private String msg;

  ResponseEnum(int status, int code, String msg) {
    this.status = status;
    this.code = code;
    this.msg = msg;
  }
}
