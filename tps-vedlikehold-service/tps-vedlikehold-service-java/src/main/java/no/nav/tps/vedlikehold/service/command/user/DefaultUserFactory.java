package no.nav.tps.vedlikehold.service.command.user;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;
import org.springframework.stereotype.Component;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@Component
public class DefaultUserFactory implements UserFactory {

    @Override
    public User createUser(UserFactoryStrategy strategy) {
        return new User(
                strategy.getDistinguishedName(),
                strategy.getUsername(),
                strategy.getRoles(),
                strategy.getToken()
        );
    }
}
