package com.sample.manish.sleuthtest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration class.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket newsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("BetSync Adapter Service V2")
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("BetSync Adapter Service V2")
            .description("BetSync Adapter V2 API with Swagger")
            .termsOfServiceUrl("TBD")
            .license("TBD")
            .licenseUrl("TBD")
            .version("2.0")
            .build();
    }
}
