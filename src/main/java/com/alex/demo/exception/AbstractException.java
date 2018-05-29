package com.alex.demo.exception;


import com.alex.demo.response.ErrorResponse;
import com.alex.demo.response.ResponseEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class AbstractException extends RuntimeException {

  private int status;
  private ErrorResponse response;

  AbstractException(ResponseEnum response) {
    this.status = response.getStatus();
    this.response = new ErrorResponse(response);
  }

  /**
   * only specific exception need extra exception info
   *
   * @param ex extra exception
   */
  public AbstractException with(Exception ex) {
    super.setStackTrace(ex.getStackTrace());
    return with(ex.getMessage());
  }

  /**
   * with more subscription messages to show
   */
  public AbstractException with(String... messages) {
    response.with(messages);
    return this;
  }

  /**
   * fast way with single message
   */
  public AbstractException with(String message) {
    response.with(message);
    return this;
  }

}
