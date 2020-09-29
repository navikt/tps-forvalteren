package no.nav.tps.forvalteren.provider.rs.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import no.nav.tps.forvalteren.consumer.rs.kodeverk.KodeverkConsumer;
import no.nav.tps.forvalteren.provider.rs.api.v1.documentation.OpenApiConfig;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.forvalteren.provider.rs.api.v1.endpoints.advices.HttpExceptionAdvice;
import no.nav.tps.forvalteren.provider.rs.naisendpoints.NaisEndpointController;
import no.nav.tps.forvalteren.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.forvalteren.provider.rs.security.config.WebSecurityConfig;
import no.nav.tps.forvalteren.service.config.ServiceConfig;

@Configuration
@Import({
        ServiceConfig.class,
        OpenApiConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class
})
@ComponentScan(basePackageClasses = {
        UserController.class,
        NaisEndpointController.class,
        HttpExceptionAdvice.class,
        KodeverkConsumer.class
})
public class RestProviderConfig {

}
