package no.nav.tps.vedlikehold.service.command.authorisation;

import java.util.Collection;

import no.nav.tps.vedlikehold.service.command.authorisation.strategies.AuthorisationServiceStrategy;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface AuthorisationService {
    boolean isAuthorised(Collection<AuthorisationServiceStrategy> strategies);
}
