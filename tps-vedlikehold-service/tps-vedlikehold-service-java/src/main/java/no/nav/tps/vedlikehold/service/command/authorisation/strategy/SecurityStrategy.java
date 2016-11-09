package no.nav.tps.vedlikehold.service.command.authorisation.strategy;

import no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.AuthorisationStrategy;

import java.util.Set;

/**
 * Created by f148888 on 08.11.2016.
 */
public interface SecurityStrategy {
    boolean isSupported(AuthorisationStrategy a1);
    void authorise(Set<String> roles, String requiredParam);
}
