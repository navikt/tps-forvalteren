package no.nav.tps.forvalteren.config.jpa;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import no.nav.tps.forvalteren.repository.jpa.UsernameResolver;

@Component
@Primary
public class UsernameTestResolver implements UsernameResolver {

    public static final String USERNAME = "usr";

    @Override
    public String getUsername() {
        return USERNAME;
    }

}
