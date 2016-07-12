package no.nav.tps.vedlikehold.provider.rs;


import no.nav.tps.vedlikehold.provider.rs.api.v1.endpoints.UserController;
import no.nav.tps.vedlikehold.provider.rs.api.v1.documentation.SwaggerConfig;
import no.nav.tps.vedlikehold.provider.rs.security.config.RestSecurityConfig;
import no.nav.tps.vedlikehold.provider.rs.security.config.WebSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.resourceresolver.FileResourceResolver;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@EnableAutoConfiguration
@Configuration
@Import({
        SwaggerConfig.class,
        WebSecurityConfig.class,
        RestSecurityConfig.class
})
@ComponentScan(basePackageClasses = {
        UserController.class
})
public class RestProviderConfig {

    @Bean
    public TemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        TemplateEngine templateEngine = new TemplateEngine();

        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

    @Bean
    public TemplateResolver templateResolver() {
        TemplateResolver resolver = new TemplateResolver();

        IResourceResolver resourceResolver = new ClassLoaderResourceResolver();
        resolver.setResourceResolver(resourceResolver);

        resolver.setPrefix("templates/");
        resolver.setSuffix(".xml");
        resolver.setTemplateMode("XML");
        resolver.setCacheable(false); //FIXME: Only development

        return resolver;
    }

    public static void main(String[] args) {
        SpringApplication.run(RestProviderConfig.class, args);
    }
}
