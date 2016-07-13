package no.nav.tps.vedlikehold.common.java.config;

import no.nav.tps.vedlikehold.common.java.message.MessageProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kristian Kyvik (Visma Consulting AS).
 */
@Configuration
@ComponentScan(basePackageClasses = {
        //LogExceptions.class,
        MessageProvider.class
})
//@Import(MapperConfig.class)
public class CommonConfig {
}
