package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface TpsAuthorisationService {
    boolean authoriseRequest(TpsServiceRoutine serviceRoutine, TpsRequest request, User user);
}
