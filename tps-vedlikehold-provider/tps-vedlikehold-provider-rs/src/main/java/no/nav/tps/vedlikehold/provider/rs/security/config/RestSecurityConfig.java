package no.nav.tps.vedlikehold.provider.rs.security.config;

import no.nav.tps.vedlikehold.provider.rs.security.PackageMarker;
import no.nav.tps.vedlikehold.provider.rs.security.mapping.RestAuthoritiesMapper;
import no.nav.tps.vedlikehold.provider.rs.security.mapping.RestUserDetailsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Tobias Hansen (Visma Consulting AS)
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackageClasses = {
        PackageMarker.class
})
public class RestSecurityConfig {

    @Value("${ldap.url}")
    private String ldapUrl;

    @Value("${ldap.domain}")
    private String ldapDomain;

    @Bean
    ActiveDirectoryLdapAuthenticationProvider authenticationProvider() {
        ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);
        provider.setAuthoritiesMapper(authoritiesMapper());
        provider.setUserDetailsContextMapper(userDetailsMapper());
        provider.setUseAuthenticationRequestCredentials(true);
        provider.setConvertSubErrorCodesToExceptions(true);
        return provider;
    }

    @Bean
    RestAuthoritiesMapper authoritiesMapper() {
        return new RestAuthoritiesMapper();
    }

    @Bean
    RestUserDetailsMapper userDetailsMapper() {
        return new RestUserDetailsMapper();
    }

    @Bean
    CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**");
            }
        };
    }
}
