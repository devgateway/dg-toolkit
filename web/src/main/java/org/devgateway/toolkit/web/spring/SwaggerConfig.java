package org.devgateway.toolkit.web.spring;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi cashewApi() {
        return GroupedOpenApi.builder()
                .group("dgtoolkit-api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi manageApi() {
        return GroupedOpenApi.builder()
                .group("dgtoolkit-manage")
                .pathsToMatch("/manage/**")
                .build();
    }

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("DG Toolkit API")
                        .description("DG Toolkit endpoints")
                        .version("1.0")
                        .license(new License().name("MIT License").url("https://opensource.org/licenses/MIT")));
    }
}
