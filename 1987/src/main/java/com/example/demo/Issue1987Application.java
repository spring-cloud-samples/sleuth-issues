package com.example.demo;

import brave.http.HttpTracing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.brave.ReactorNettyHttpTracing;

@SpringBootApplication
public class Issue1987Application {

    public static void main(String[] args) {
        SpringApplication.run(Issue1987Application.class, args);
    }

    // not necessary to reproduce the problem, but when configured StackOverflowError occurs at startup
    @Configuration
    public static class Config {
        @Bean
        public NettyServerCustomizer tracingCustomizer(HttpTracing httpTracing) {
            return server -> ReactorNettyHttpTracing.create(httpTracing).decorateHttpServer(server);
        }
    }
}
