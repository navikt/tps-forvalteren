package no.nav.tps.forvalteren.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class UsernameAuditingAware implements AuditorAware<String> {

    @Autowired
    private UsernameResolver resolver;

    @Override
    public String getCurrentAuditor() {
        return resolver.getUsername();
    }
}
