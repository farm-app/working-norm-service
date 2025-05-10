package ru.rtln.workingnormservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
public class RestTemplateConfig {

    @Value("${internal.service.auth.url}")
    private String authServicePath;

    @Bean
    public RestTemplate authRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .rootUri(authServicePath)
                .setConnectTimeout(Duration.of(10, ChronoUnit.SECONDS))
                .build();
    }
}
