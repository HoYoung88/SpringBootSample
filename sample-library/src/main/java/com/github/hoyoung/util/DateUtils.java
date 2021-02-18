package com.github.hoyoung.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by HoYoung on 2021/02/05.
 */
public class DateUtils {
  public static ZoneId DEFUALT_ZONE_ID = ZoneId.of("Asia/Seoul");

  public static String DEFUALT_DATE_FROMAT_YYYY_MM_DD = "yyyy-MM-dd";
  public static String DEFUALT_DATE_FROMAT_YYYYMMDD = "yyyyMMdd";
  public static String DEFUALT_DATE_FROMAT_YYYYMMDDHHMM = "yyyyMMddhhmm";
  public static String DEFUALT_DATE_FROMAT_YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
  public static String DEFUALT_DATE_FROMAT = "yyyy-MM-dd hh:mm:ss";

  public static DateTimeFormatter DEFUALT_DATE_FORMATTER_YYYY_MM_DD = DateTimeFormatter
      .ofPattern(DEFUALT_DATE_FROMAT_YYYY_MM_DD);

  public static DateTimeFormatter DEFUALT_DATE_FORMATTER_YYYYMMDD = DateTimeFormatter
      .ofPattern(DEFUALT_DATE_FROMAT_YYYYMMDD);

  public static DateTimeFormatter DEFUALT_DATE_FORMATTER_YYYYMMDDHHMM = DateTimeFormatter
      .ofPattern(DEFUALT_DATE_FROMAT_YYYYMMDDHHMM);

  public static DateTimeFormatter DEFUALT_DATE_FORMATTER_YYYYMMDDHHMMSS = DateTimeFormatter
      .ofPattern(DEFUALT_DATE_FROMAT_YYYYMMDDHHMMSS);

  public static DateTimeFormatter DEFUALT_DATE_TIME_FORMATTER = DateTimeFormatter
      .ofPattern(DEFUALT_DATE_FROMAT);

  //---

  public static LocalDate toNowDate() {
    return LocalDate.now(DEFUALT_ZONE_ID);
  }

  public static LocalDate asLocalDate(String date) {
    return asLocalDate(date, DEFUALT_DATE_FORMATTER_YYYY_MM_DD);
  }

  public static LocalDate asLocalDate(String date, DateTimeFormatter dateTimeFormatter) {
    return LocalDate.parse(date, dateTimeFormatter);
  }

  public static LocalDate asLocalDate(int year, int month, int day) {
    return LocalDate.of(year, month, day);
  }

  public static LocalDate asLocalDate(LocalDateTime localDateTime) {
    return localDateTime.toLocalDate();
  }

  //---

  public static LocalDateTime toNowDateTime() {
    return LocalDateTime.now(DEFUALT_ZONE_ID);
  }

  public static LocalDateTime toNowDateTime(int hour, int minute, int second) {
    return LocalDateTime.now(DEFUALT_ZONE_ID).withHour(hour).withMinute(minute).withSecond(second);
  }

  public static LocalDateTime asLocalDateTime(String dateTime) {
    return asLocalDateTime(dateTime, DEFUALT_DATE_TIME_FORMATTER);
  }

  public static LocalDateTime asLocalDateTime(String dateTime, DateTimeFormatter dateTimeFormatter) {
    return LocalDateTime.parse(dateTime, dateTimeFormatter);
  }

  public static LocalDateTime asLocalDateTime(int year, int month, int day,
      int hour, int minute, int second) {
    return asLocalDate(year, month, day).atTime(hour, minute, second);
  }

  public static Date asDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(DEFUALT_ZONE_ID).toInstant());
  }

}
