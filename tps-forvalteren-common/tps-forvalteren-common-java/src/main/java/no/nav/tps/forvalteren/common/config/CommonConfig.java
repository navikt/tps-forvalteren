package no.nav.tps.forvalteren.common.config;

import java.time.Clock;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;

import no.nav.tps.forvalteren.common.mapping.MapperConfig;
import no.nav.tps.forvalteren.common.message.MessageProvider;
import no.nav.tps.forvalteren.common.tpsapi.TpsPropsService;

@Configuration
@ComponentScan(basePackageClasses = {
        MessageProvider.class
})
@Import({MapperConfig.class, TpsPropsService.class })
public class CommonConfig {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}