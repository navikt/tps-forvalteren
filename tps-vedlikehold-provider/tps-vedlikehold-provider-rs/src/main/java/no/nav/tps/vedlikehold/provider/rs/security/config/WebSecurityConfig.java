package no.nav.tps.vedlikehold.provider.rs.security.config;

import no.nav.tps.vedlikehold.provider.rs.security.csrf.CsrfHeaderFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${tps.vedlikehold.cookie.path}")
    private String cookiePath;

    @Autowired
    private ActiveDirectoryLdapAuthenticationProvider authenticationProvider;

    @Autowired
    private CsrfTokenRepository tokenRepository;

    @Autowired
    private RestAuthenticationEntryPoint authentificationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(authenticationProvider);
    }

    //TODO SETT -->CSRF<-- Til Enable igjen når man er ferdig med å teste med Postman
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                .antMatchers("/api/**").hasAnyRole("ACCESS")
                .and().httpBasic()
                .and()
                .csrf()
                .csrfTokenRepository(tokenRepository)
                .and()
                .addFilterAfter(new CsrfHeaderFilter(cookiePath), CsrfFilter.class);

        http.exceptionHandling().authenticationEntryPoint(authentificationEntryPoint);

        http.csrf().disable();  //TODO REMOVE LATER
    }
}