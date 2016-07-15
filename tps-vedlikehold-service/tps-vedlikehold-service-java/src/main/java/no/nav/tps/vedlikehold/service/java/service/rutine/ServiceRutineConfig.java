package no.nav.tps.vedlikehold.service.java.service.rutine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Configuration
@ComponentScan
public class ServiceRutineConfig {

    private static final String TEMPLATE_PREFIX = "templates/";
    private static final String TEMPLATE_SUFFIX = ".xml";
    private static final String TEMPLATE_MODE   = "XML";

    @Bean
    public TemplateEngine templateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();

        templateEngine.setTemplateResolver(templateResolver());

        return templateEngine;
    }

    public TemplateResolver templateResolver() {
        TemplateResolver resolver           = new TemplateResolver();
        IResourceResolver resourceResolver  = new ClassLoaderResourceResolver();

        resolver.setResourceResolver(resourceResolver);

        resolver.setPrefix(TEMPLATE_PREFIX);
        resolver.setSuffix(TEMPLATE_SUFFIX);
        resolver.setTemplateMode(TEMPLATE_MODE);

        return resolver;
    }

}
