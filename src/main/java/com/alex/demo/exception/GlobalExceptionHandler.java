package com.alex.demo.exception;

import com.alex.demo.response.ErrorResponse;
import com.alex.demo.response.ResponseEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  private static final String END_LINE = "====================================TRACE END====================================";
  private static final String TRACE_FLAG = "alex";

  private boolean test = true;

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity<Object> handleException(ServletWebRequest req, java.lang.Exception ex) {
    AbstractException msg;
    // 自定义异常处理
    if (ex instanceof AbstractException) {
      msg = (AbstractException) ex;
    } else if (ex instanceof NullPointerException) {
      // 系统异常处理
      msg = Exception.of(ResponseEnum.NULL_POINTER_EXCEPTION);
    } else if (ex instanceof IllegalArgumentException
        || ex instanceof MissingServletRequestParameterException) {
      // 参数无效
      msg = Exception.badRequest();
    } else if (ex instanceof AccessDeniedException) {
      msg = Exception.badRequest("权限禁止,检查URL传参");
    } else if (ex instanceof DataIntegrityViolationException) {
      // DB插入重复
      msg = Exception.of(ResponseEnum.DB_EXCEPTION);
    } else if (ex instanceof BadSqlGrammarException) {
      // 系统异常处理
      msg = Exception.of(ResponseEnum.DB_EXCEPTION);
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      // 参数类型错误
      msg = Exception.of(ResponseEnum.INTERNAL_SERVER_ERROR);
    } else {
      // 记录其他未知异常 Log
      msg = Exception.of(ResponseEnum.INTERNAL_SERVER_ERROR);
    }

    msg.with(ex.getMessage());

    // get response from custom exception
    ErrorResponse res = msg.getResponse();

    // format exception traces with request data
    String[] traceMeta = expMeta(req, ex);
    String[] traceData = parseTrace(ex.getStackTrace());
    String[] trace = ArrayUtils.addAll(traceMeta, traceData);

    // log exceptions
    log.debug(res.getMsg() + "\n" + String.join("\n", trace) + "\n" + END_LINE);

    // set traces
    if (test) {
      res.setTrace(trace);
    }
    return ResponseEntity.ok(res);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleException((ServletWebRequest) request, ex);
  }

  private String[] expMeta(ServletWebRequest req, java.lang.Exception ex) {
    return new String[]{
        "HOST: " + req.getRequest().getRemoteHost(),
        "URI: " + req.getRequest().getRequestURI(),
        "METHOD: " + req.getHttpMethod(),
        "PARAMS: " + printParameterMap(req.getParameterMap()),
        "REQ: " + MDC.get("REQ"),
        "CAUSE: " + ex.toString()
    };
  }

  private String printParameterMap(Map<String, String[]> map) {
    if (map.isEmpty()) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String[]> e : map.entrySet()) {
      sb.append(e.getKey()).append('=');
      sb.append(Arrays.toString(e.getValue())).append('&');
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  /**
   * parse stack traces with most important infos
   */
  private String[] parseTrace(StackTraceElement[] stackTraces) {
    if (stackTraces[0].toString().contains(TRACE_FLAG)) {
      List<String> customTraces = new ArrayList<>();
      for (StackTraceElement element : stackTraces) {
        String trace = element.toString();
        if (trace.contains(TRACE_FLAG)) {
          customTraces.add(trace);
        } else {
          break;
        }
      }
      return customTraces.toArray(new String[customTraces.size()]);
    } else {
      return Arrays.stream(stackTraces).limit(13).map(Object::toString).toArray(String[]::new);
    }
  }

}
