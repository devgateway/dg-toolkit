package org.devgateway.toolkit.web.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    @Bean
    public Docket yaliApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("Api").apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.any()).paths(regex("/api/.*")).build();
    }

    @Bean
    public Docket manageApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("manageApi")
                .select().apis(RequestHandlerSelectors.any()).paths(regex("/manage/.*")).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("DG-Toolkit Application API")
                .description("Endpoints description").license("MIT License")
                .licenseUrl("https://opensource.org/licenses/MIT").version("1.0").build();
    }

}