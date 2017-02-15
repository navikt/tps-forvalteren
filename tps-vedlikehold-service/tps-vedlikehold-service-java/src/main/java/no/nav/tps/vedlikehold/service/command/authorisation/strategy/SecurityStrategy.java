package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.AuthorisationStrategy;

import java.util.Set;

/**
 * Created by f148888 on 08.11.2016.
 */
public interface SecurityStrategy {
    boolean isSupported(AuthorisationStrategy a1);
    void handleUnauthorised(Set<String> roles, String requiredParam);
    boolean isAuthorised(Set<String> roles, String requiredParam);
}
