package com.alex.demo.exception;


import com.alex.demo.response.ResponseEnum;

public class Exception extends AbstractException {

  private static final long serialVersionUID = 1L;

  private Exception(ResponseEnum msg) {
    super(msg);
  }

  // client exceptions, others use of method to create

  public static Exception badRequest() {
    return new Exception(ResponseEnum.BAD_REQUEST);
  }

  public static AbstractException badRequest(String msg) {
    return new Exception(ResponseEnum.BAD_REQUEST).with(msg);
  }

  public static Exception forbidden() {
    return new Exception(ResponseEnum.FORBIDDEN);
  }

  public static AbstractException forbidden(String msg) {
    return new Exception(ResponseEnum.FORBIDDEN).with(msg);
  }

  public static Exception unauthorized() {
    return new Exception(ResponseEnum.UNAUTHORIZED);
  }

  public static Exception notFound() {
    return new Exception(ResponseEnum.NOT_FOUND);
  }

  public static Exception tooManyRequests() {
    return new Exception(ResponseEnum.TOO_MANY_REQUESTS);
  }

  public static Exception of(ResponseEnum res) {
    return new Exception(res);
  }


}
