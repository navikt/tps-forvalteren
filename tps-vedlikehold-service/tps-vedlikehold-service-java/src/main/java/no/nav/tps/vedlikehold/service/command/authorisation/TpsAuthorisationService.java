package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.command.User.User;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

public interface TpsAuthorisationService {
    void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine, String environment, User user);
    boolean isAuthorisedToSeePerson(TpsServiceRoutineDefinition serviceRoutine, String fnr, User user);
}
