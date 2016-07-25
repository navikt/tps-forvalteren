package no.nav.tps.vedlikehold.service.command.user;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface UserFactory {
    User createUser(UserFactoryStrategy strategy);
}
