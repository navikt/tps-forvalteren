package no.nav.tps.forvalteren.repository.jpa.auditing;

import no.nav.tps.forvalteren.repository.jpa.UsernameResolver;
import org.springframework.stereotype.Component;

@Component
public class UsernameTestResolver implements UsernameResolver {

    public static final String USERNAME = "usr";

    @Override
    public String getUsername() {
        return USERNAME;
    }

}
