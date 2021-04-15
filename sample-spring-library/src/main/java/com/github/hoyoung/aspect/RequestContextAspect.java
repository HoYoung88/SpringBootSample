package com.github.hoyoung.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

/**
 * Created by HoYoung on 2021/01/27.
 */
@Component
@Aspect
public class RequestContextAspect {
  private static final Logger log = LoggerFactory.getLogger("request.parameter");
  private static final ZoneId KOREA_ZONE_ID = ZoneId.of("Asia/Seoul");
  private static final DateTimeFormatter DEFAULT_DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private final StringBuilder sb = new StringBuilder();
  private final String hyphen = StringUtils.rightPad("", 120, "-");

  @Pointcut("execution(* (@org.springframework.web.bind.annotation.RestController *).*(..))")
  public void loggerPointCut() {}

  @Around("loggerPointCut()")
  public Object task(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object result = null;
    StopWatch sw = new StopWatch();

    String target = proceedingJoinPoint.getSignature().getDeclaringTypeName()
        .concat(".")
        .concat(proceedingJoinPoint.getSignature().getName());

    HttpServletRequestWrapper request = new HttpServletRequestWrapper(
        ((ServletRequestAttributes) Objects
            .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());

    try {
      String requestTime = LocalDateTime.now(KOREA_ZONE_ID).format(DEFAULT_DATA_TIME_FORMATTER);
      sb.append(hyphen).append(System.lineSeparator())
          .append(LoggerAppender.message("request time", requestTime))
          .append(System.lineSeparator())
          .append(LoggerAppender.message("target", target))
          .append(System.lineSeparator())
          .append(LoggerAppender.message("url", request.getRequestURL().insert(0, "[".concat(request.getMethod()).concat("] ")).toString()))
          .append(System.lineSeparator())
          .append(LoggerAppender.message("request ip", this.getClientIP(request)))
          .append(System.lineSeparator());

      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String keyName = headerNames.nextElement();
        sb.append(LoggerAppender.message(keyName, request.getHeader(keyName)))
            .append(System.lineSeparator());
      }

      Enumeration<String> parameterNames = request.getParameterNames();
      String contentType = Optional.ofNullable(request.getContentType()).orElseGet(() -> "");

      if(contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
        parameterNames = null;
        String jsonString = new BufferedReader(
            new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))
            .lines().collect(Collectors.joining(System.lineSeparator()));
        sb.append(LoggerAppender.message("body", new ObjectMapper().readTree(jsonString)))
            .append(System.lineSeparator());;
      }

      if(contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
        parameterNames = null;
        MultipartHttpServletRequest multipartHttpServletRequest = new StandardMultipartHttpServletRequest(request);
        Collection<Part> parts = multipartHttpServletRequest.getParts();

        parts.forEach(item -> {
          String keyName = item.getName();
          StringBuilder mSb = new StringBuilder().append("[").append(keyName).append("] ");

          if(ObjectUtils.isEmpty(item.getContentType())) {
            mSb.append(multipartHttpServletRequest.getParameter(keyName));
          } else {
            mSb.append(item.getContentType());
          }

          sb.append(LoggerAppender.message("parameter", mSb.toString()))
              .append(System.lineSeparator());
        });
      }

      if(!ObjectUtils.isEmpty(parameterNames)) {
        while (parameterNames.hasMoreElements()) {
          String keyName = parameterNames.nextElement();
          String keyAndValue = new StringBuilder()
              .append("[")
              .append(keyName)
              .append("] ")
              .append(request.getParameter(keyName))
              .toString();
          sb.append(LoggerAppender.message("parameter", keyAndValue))
              .append(System.lineSeparator());
        }
      }

      log.info("{}", sb.toString());
      sb.setLength(0);

      sw.start();
      result = proceedingJoinPoint.proceed();

    } catch (Throwable throwable) {
//      throwable.printStackTrace();
//      log.error("{}", throwable.fillInStackTrace());
      throw throwable;
    } finally {
      // Controller 로직 실행 시간
      sw.stop();
      log.info("{}", LoggerAppender.message("execution-time", sw.getTotalTimeMillis()).append(" (ms)"));
    }

    return result;
  }

  private String getClientIP(HttpServletRequest request) {
    String remoteAddr = "";

    if (request != null) {
      remoteAddr = request.getHeader("X-FORWARDED-FOR");
      if (remoteAddr == null || "".equals(remoteAddr)) {
        remoteAddr = request.getRemoteAddr();
      }
    }
    return remoteAddr;
  }

  static class LoggerAppender {
    public static StringBuffer message(String key, Object message) {
      return new StringBuffer()
          .append("--- [")
          .append(StringUtils.leftPad(key, 20))
          .append("] > ")
          .append(message);
    }
  }
}
