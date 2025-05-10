package ru.rtln.workingnormservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "cookie_auth",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.COOKIE,
        paramName = "access_token"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Working Norm Service",
                description = "Product part of Working Norm service",
                version = "1.0.0"
        ),
        security = @SecurityRequirement(name = "cookie_auth")
)
@Configuration
public class OpenApiConfig {

}
