package no.nav.tps.vedlikehold.provider.rs.security.config;

import no.nav.tps.vedlikehold.provider.rs.security.csrf.CsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;

/**
 * Created by Tobias Hansen (Visma Consulting AS)
 */

@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ActiveDirectoryLdapAuthenticationProvider authenticationProvider;

    @Autowired
    private CsrfTokenRepository tokenRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**")
                .permitAll()
                .antMatchers("/index.html") // Should access to the web module be configured in the rest module?
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/user")
                .permitAll()
                .antMatchers("/api/**")
                .hasRole("READ")
                .and().httpBasic()
                .and()
                .csrf()
                .csrfTokenRepository(tokenRepository)
                .and()
                .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
    }
}