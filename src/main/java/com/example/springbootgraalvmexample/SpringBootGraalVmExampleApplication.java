package com.example.springbootgraalvmexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static java.util.Collections.singletonMap;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class SpringBootGraalVmExampleApplication {

  @Bean
  RouterFunction<ServerResponse> routes() {
    return route().path("/**",
                        builder -> builder.GET("/**",
                                               request -> ok().contentType(APPLICATION_JSON)
                                                              .body(singletonMap("result", "Hello, World!"))))
                  .build();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringBootGraalVmExampleApplication.class, args);
  }
}
