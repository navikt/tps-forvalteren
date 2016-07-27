package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.authorisation.User;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface TpsAuthorisationService extends AuthorisationService {
    public Boolean userIsAuthorisedToReadPersonInEnvironment(User user, String fnr, String environment);
}
