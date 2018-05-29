package com.alex.demo.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

/**
 * 返回工具
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TResponse {

  public static ResponseEntity success() {
    return ResponseEntity.ok(ResponseEnum.SUCCESS);
  }

  public static ResponseEntity success(Object data) {
    return ResponseEntity.ok(new SuccessResponse(data));
  }

}
