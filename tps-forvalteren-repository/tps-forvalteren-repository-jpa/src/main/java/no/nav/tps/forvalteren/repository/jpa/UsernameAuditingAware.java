package no.nav.tps.forvalteren.repository.jpa;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class UsernameAuditingAware implements AuditorAware<String> {

    @Autowired
    private UsernameResolver resolver;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(resolver.getUsername());
    }
}
