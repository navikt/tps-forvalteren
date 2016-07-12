package no.nav.tps.vedlikehold.provider.web.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *  @author Kristian Kyvik (Visma Consulting).
 */
@Configuration
@EnableAutoConfiguration
@Import(ApplicationConfig.class)
public class ApplicationServletInitializer extends SpringBootServletInitializer {
    /**
     * Configures the default Spring Boot servlet &quot;dispatcherServlet&quot; to expose resources on /internal.
     */
    @Bean
    WebMvcConfigurerAdapter dispatcherServletConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/internal/*");
            }
        };
    }

}
