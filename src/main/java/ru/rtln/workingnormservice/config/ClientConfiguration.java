package ru.rtln.workingnormservice.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    private static final String API_KEY_HEADER = "Api-key";

    @Value("${internal.service.auth.secure-key}")
    public String authSecretKeyValue;

    @Bean
    public RequestInterceptor authHeaderRequestInterceptor() {
        return template -> template.header(API_KEY_HEADER, authSecretKeyValue);
    }

}
