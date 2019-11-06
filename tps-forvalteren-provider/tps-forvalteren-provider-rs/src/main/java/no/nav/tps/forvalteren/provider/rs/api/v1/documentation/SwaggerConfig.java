package no.nav.tps.forvalteren.provider.rs.api.v1.documentation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configure automated swagger API documentation
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${application.version}")
    private String appVersion;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(ApiIgnore.class)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("TPS-Forvalteren")
                .description("TPS-Forvalteren")
                .version(appVersion)
                .termsOfServiceUrl("https://nav.no")
                .contact(new Contact("Visma",
                        "http://stash.devillo.no/projects/FEL/repos/tps-forvalteren/", "nav.no"))
                .license("Super Strict Licence")
                .licenseUrl("https://opensource.org/licenses/super-strict-license")
                .build();
    }

    @Bean
    UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.LIST)
                .displayRequestDuration(true)
                .validatorUrl(null)
                .build();
    }
}
