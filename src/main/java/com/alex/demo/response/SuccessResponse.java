package com.alex.demo.response;

import lombok.Data;

/**
 * 对象返回
 */
@Data
public class SuccessResponse implements Response {

  /**
   * success code is 200
   */
  private int code = 200;
  private Object data;

  protected SuccessResponse(Object data) {
    this.data = data;
  }

}
