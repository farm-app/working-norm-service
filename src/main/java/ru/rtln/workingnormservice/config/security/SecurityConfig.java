package ru.rtln.workingnormservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.rtln.common.security.keycloak.auth.AuthenticationProviderFilter;
import ru.rtln.common.security.secret.key.SecretKeyAuthenticationFilter;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final String[] PERMIT_ALL_ENDPOINTS = new String[]{
            "/error/**",
            "/swagger-ui/**",
            "/api-docs/**"
    };

    public static final String[] INTERNAL_ENDPOINTS = {
            "/reports/**"
    };

    @Value("${internal.api-key-header-name}")
    private String apiKeyHeaderName;

    @Value("${internal.service.auth.secure-key}")
    private String authServiceSecureKey;

    private final RestTemplate authRestTemplate;

    @Bean
    public AuthenticationProviderFilter authenticationProviderFilter() {
        return new AuthenticationProviderFilter(authRestTemplate, apiKeyHeaderName, authServiceSecureKey);
    }

    @Bean
    public FilterRegistrationBean<AuthenticationProviderFilter> registration(AuthenticationProviderFilter filter) {
        FilterRegistrationBean<AuthenticationProviderFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    @Order(0)
    public SecurityFilterChain internalFilterChain(HttpSecurity http,
                                                   @Value("${internal.api-key-header-name}") String apiKeyHeaderName,
                                                   @Value("${internal.service.user.secure-key}") String userSecretKeyValue) throws Exception {

        AccessDeniedHandler accessDeniedHandler = (request, response, exception) -> response.setStatus(SC_FORBIDDEN);
        var usernamePasswordFilter = UsernamePasswordAuthenticationFilter.class;

        http.securityMatcher(INTERNAL_ENDPOINTS)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(INTERNAL_ENDPOINTS).authenticated()
                        .anyRequest().permitAll()
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exceptionConfigurer -> exceptionConfigurer
                        .authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(new SecretKeyAuthenticationFilter(apiKeyHeaderName, userSecretKeyValue), usernamePasswordFilter);

        return http.build();
    }


    @Bean
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity http,
                                               OncePerRequestFilter authenticationProviderFilter) throws Exception {
        AccessDeniedHandler accessDeniedHandler = (request, response, exception) -> response.setStatus(SC_FORBIDDEN);
        http
                .securityMatcher("/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        .requestMatchers(PERMIT_ALL_ENDPOINTS).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED))
                        .accessDeniedHandler(accessDeniedHandler));
        http.addFilterBefore(authenticationProviderFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
