package no.nav.tps.forvalteren.provider.rs.security.config;

import no.nav.tps.forvalteren.provider.rs.security.csrf.CsrfHeaderFilter;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Component
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private String cookiePath="/";

    @Value("${tpsf.security.cors.origins: ''}")
    private String[] allowedOrigins;

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

//        http.authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
//                .antMatchers("/api/**").hasAnyRole("ACCESS")
                //.and().httpBasic()
//                .and()
//                .csrf()
//                .csrfTokenRepository(tokenRepository)
//                .and()
//                .addFilterAfter(new CsrfHeaderFilter(cookiePath), CsrfFilter.class);

        http.exceptionHandling().authenticationEntryPoint(authentificationEntryPoint);

        http.csrf().disable();

        http.cors();

        http.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/css/**", "/internal/**");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList(
                "Access-Control-Allow-Headers",
                "Access-Control-Request-Headers",
                "Access-Control-Request-Method",
                "Access-Control-Allow-Credentials",     // Trengs for at POST,PUT, DEL skal gå igjennom med credentiasl(Cookie). GET trenger ikke det.
                "X-Requested-With",
                "X-Auth-Token",
                "X-XSRF-TOKEN",
                "Content-Type",
                "Authorization"
        ));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}