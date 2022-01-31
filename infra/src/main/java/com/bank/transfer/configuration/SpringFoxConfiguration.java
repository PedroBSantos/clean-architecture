package com.bank.transfer.configuration;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfiguration {

    public SpringFoxConfiguration() {
    }

    @Bean
    public Docket swaggerConfiguration() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.bank.transfer.controllers"))
                .paths(PathSelectors.any()).build().apiInfo(apiInfo()).useDefaultResponseMessages(false)
                .directModelSubstitute(Object.class, java.lang.Void.class);
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder().defaultModelsExpandDepth(-1).defaultModelExpandDepth(1)
                .docExpansion(DocExpansion.LIST).operationsSorter(OperationsSorter.ALPHA).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("BANK API REST", "API Rest para gerenciamento de contas", "1.0", null, null,
                null, null, Collections.emptyList());
    }
}
