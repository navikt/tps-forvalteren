package no.nav.tps.forvalteren.provider.rs.security.config;

import no.nav.tps.forvalteren.provider.rs.security.csrf.CsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;


@Component
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private String cookiePath="/";

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

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}