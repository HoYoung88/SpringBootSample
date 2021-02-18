package com.github.hoyoung.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by HoYoung on 2021/01/27.
 */
@Configuration
public class SwaggerConfig {

  @Bean
  public Docket documentDemo() {
    return new Docket(DocumentationType.SWAGGER_2)
        .produces(this.getProduces())
        .consumes(this.getConsumes())
        .apiInfo(this.getApiInfo())
        .protocols(this.getProtocols())
        .forCodeGeneration(true)
        .securitySchemes(Collections.singletonList(apiKey()))
        .securityContexts(Collections.singletonList(securityContext()))
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("JWT", "Authorization", "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
  }

  private Set<String> getProtocols() {
    return Stream.of("http", "https")
        .collect(Collectors.toCollection(HashSet::new));
  }

  private Set<String> getProduces() {
    return Stream.of(MediaType.APPLICATION_JSON_VALUE)
        .collect(Collectors.toCollection(HashSet::new));
  }

  private Set<String> getConsumes() {
    return Stream.of(MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE)
        .collect(Collectors.toCollection(HashSet::new));
  }

  private ApiInfo getApiInfo() {
    return new ApiInfoBuilder()
        .description("This is a sample server Petstore server. You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/). For this sample, you can use the api key `special-key` to test the authorization filters.")
        .version("1.0.0")
        .title("Demo Api")
        .termsOfServiceUrl("http://swagger.io/terms/")
        .contact(new Contact("name", "http://swagger.io/contact/", "apiteam@swagger.io"))
        .license("Apache 2.0")
        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
        .build();
  }

  private List<Response> getResponse() {
    return Arrays.asList(
        new ResponseBuilder().code("200").description("Success").build(),
        new ResponseBuilder().code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .description(HttpStatus.BAD_REQUEST.name()).build());
  }
}
