package no.nav.tps.forvalteren.provider.rs.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices.HttpExceptionAdvice;
import no.nav.tps.forvalteren.provider.rs.naisendpoints.NaisEndpointController;
import no.nav.tps.forvalteren.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.forvalteren.provider.rs.security.config.WebSecurityConfig;
import no.nav.tps.forvalteren.provider.rs.api.v1.documentation.SwaggerConfig;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.forvalteren.service.config.ServiceConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Configuration
@Import({
        ServiceConfig.class,
        SwaggerConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class
})
@ComponentScan(basePackageClasses = {
        UserController.class,
        NaisEndpointController.class,
        HttpExceptionAdvice.class
})
public class RestProviderConfig {

    @Component
    public class MyObjectMapper extends ObjectMapper {
        public MyObjectMapper() {
            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
            findAndRegisterModules();
        }
    }
}
