package no.nav.tps.vedlikehold.service.command.user;

import no.nav.tps.vedlikehold.domain.service.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface UserFactory {
    User createUser(UserFactoryStrategy strategy);
}