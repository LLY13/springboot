package com.alex.demo.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse implements Response {

  /**
   * custom code
   */
  private int code;
  /**
   * debug message for developers
   */
  private String msg;
  /**
   * local message for users
   */
//  private String subMsg;

  /**
   * error trace for debug
   */
  private String[] trace;

  public ErrorResponse(ResponseEnum e) {
    this.code = e.getCode();
    this.msg = e.getMsg();
  }

  public ErrorResponse(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public ErrorResponse with(String... messages) {
    StringBuilder sb = new StringBuilder(msg);
    for (String m : messages) {
      if (m != null) {
        sb.append(" - ").append(m);
      }
    }
    msg = sb.toString();
    return this;
  }

  public ErrorResponse with(String message) {
    if (message != null) {
      msg = msg + " - " + message;
    }
    return this;
  }

  /**
   * direct print this response to output stream, without traces
   */
  public String print() {
    return "{\"code\":" + code + ",\"msg\":\"" + msg + "\"}";
  }

}
