package com.reactive.programming.challenge.application.config;

import com.reactive.programming.challenge.infrastructure.adapter.util.Constants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(Constants.INFO_TITLE)
                        .version(Constants.INFO_VERSION)
                        .description(Constants.INFO_DESCRIPTION));
    }
}
