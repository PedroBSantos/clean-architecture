package com.bank.transfer.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
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
                .securityContexts(Arrays.asList(securityContext())).securitySchemes(Arrays.asList(apiKey()))
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

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    List<SecurityReference> defaultAuth() {
        var authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
