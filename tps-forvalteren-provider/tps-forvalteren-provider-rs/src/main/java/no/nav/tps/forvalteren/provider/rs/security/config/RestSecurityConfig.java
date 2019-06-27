package no.nav.tps.forvalteren.provider.rs.security.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import no.nav.tps.forvalteren.provider.rs.security.PackageMarker;
import no.nav.tps.forvalteren.provider.rs.security.authentication.TpsfLdapAuthenticationProvider;
import no.nav.tps.forvalteren.provider.rs.security.mapping.RestAuthoritiesMapper;
import no.nav.tps.forvalteren.provider.rs.security.mapping.RestUserDetailsMapper;

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
    AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        return new ProviderManager(providers);
    }

    @Bean
    RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    TpsfLdapAuthenticationProvider authenticationProvider() {
        TpsfLdapAuthenticationProvider provider = new TpsfLdapAuthenticationProvider(ldapDomain, ldapUrl);

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
}
