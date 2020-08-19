package no.nav.tps.forvalteren;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;

import no.nav.tps.forvalteren.common.java.config.CacheConfig;
import no.nav.tps.forvalteren.common.java.config.CommonConfig;
import no.nav.tps.forvalteren.common.java.mapping.MapperConfig;
import no.nav.tps.forvalteren.provider.rs.config.RestProviderConfig;
import no.nav.tps.forvalteren.repository.jpa.config.RepositoryConfig;

@Configuration
@EnableAutoConfiguration
@Import({
        CommonConfig.class,
        CacheConfig.class,
        RepositoryConfig.class,
        MapperConfig.class,
        RestProviderConfig.class
})
public class ApplicationConfig {

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(new String[] { SecurityContextHolder.MODE_INHERITABLETHREADLOCAL });
        return methodInvokingFactoryBean;
    }
}
