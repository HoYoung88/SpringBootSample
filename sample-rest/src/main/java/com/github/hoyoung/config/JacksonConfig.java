package com.github.hoyoung.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by HoYoung on 2021/04/12.
 */
@Configuration
public class JacksonConfig {
  @Bean
  public Module jsonMapperJava8DateTimeModule() {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

    module.addDeserializer(LocalDate.class, new LocalDateDeserializer(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    module.addSerializer(LocalDate.class, new LocalDateSerializer(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    return module;
  }
}
