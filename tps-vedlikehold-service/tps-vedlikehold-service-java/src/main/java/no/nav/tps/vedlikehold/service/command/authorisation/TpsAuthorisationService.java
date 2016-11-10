package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.TpsRequest;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */
public interface TpsAuthorisationService {
    //boolean userIsAuthorisedToReadPersonInEnvironment(TpsServiceRoutine serviceRoutine, TpsRequest request, User user);
    void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine, String environment, User user);
    void authoriseFodselsnummer(TpsServiceRoutineDefinition serviceRoutine, String fnr, User user);
}
