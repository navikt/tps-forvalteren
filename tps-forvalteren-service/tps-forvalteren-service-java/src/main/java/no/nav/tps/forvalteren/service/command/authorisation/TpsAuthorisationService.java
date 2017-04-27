package no.nav.tps.forvalteren.service.command.authorisation;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

public interface TpsAuthorisationService {

    void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine);

    void authorisePersonSearch(TpsServiceRoutineDefinition serviceRoutine, String fnr);

    boolean isAuthorisedToUseServiceRutine(TpsServiceRoutineDefinition serviceRoutine);

    boolean isAuthorisedToFetchPersonInfo(TpsServiceRoutineDefinition serviceRoutine, String fnr);

}
