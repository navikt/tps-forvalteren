package no.nav.tps.forvalteren.provider.rs.security.user;

import no.nav.tps.forvalteren.repository.jpa.UsernameResolver;
import no.nav.tps.forvalteren.service.user.UserContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityUsernameResolver implements UsernameResolver {

    @Autowired
    private UserContextHolder contextHolder;

    @Override
    public String getUsername() {
        return contextHolder.getUsername();
    }
}
