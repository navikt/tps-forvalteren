package no.nav.tps.forvalteren.provider.rs.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices.HttpExceptionAdvice;
import no.nav.tps.forvalteren.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.forvalteren.provider.rs.security.config.WebSecurityConfig;
import no.nav.tps.forvalteren.provider.rs.api.v1.documentation.SwaggerConfig;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.forvalteren.service.config.ServiceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;



@Configuration
@Import({
        ServiceConfig.class,
        SwaggerConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class
})
@ComponentScan(basePackageClasses = {
        UserController.class,
        HttpExceptionAdvice.class
})
public class RestProviderConfig {

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return mapper;
    }
}
