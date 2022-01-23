package com.ssafy.auth.swagger.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebMvc
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(initApi())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ssafy.auth"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo initApi() {
        return new ApiInfoBuilder()
                .title("Auth server API with Swagger")
                .description("추가 예정")
                .version("1.0.0")
                .build();
    }
}
