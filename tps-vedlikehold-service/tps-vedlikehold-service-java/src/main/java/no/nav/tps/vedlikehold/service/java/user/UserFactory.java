package no.nav.tps.vedlikehold.service.java.user;

import no.nav.tps.vedlikehold.domain.service.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface UserFactory {
    User createUser(UserFactoryStrategy strategy);
}
