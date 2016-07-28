package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.service.command.authorisation.strategies.AuthorisationServiceStrategy;

import java.util.Collection;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
@FunctionalInterface
public interface AuthorisationService {
    Boolean isAuthorised(Collection<AuthorisationServiceStrategy> strategies);
}
