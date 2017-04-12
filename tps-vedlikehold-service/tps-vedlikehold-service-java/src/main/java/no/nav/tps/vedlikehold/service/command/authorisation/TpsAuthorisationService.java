package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

public interface TpsAuthorisationService {

    void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine);

    boolean isAuthorisedToUseServiceRutine(TpsServiceRoutineDefinition serviceRoutine);

    boolean isAuthorisedToSeePerson(TpsServiceRoutineDefinition serviceRoutine, String fnr);
}
