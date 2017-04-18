package no.nav.tps.vedlikehold.service.command.authorisation;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

public interface TpsAuthorisationService {

    void authoriseRestCall(TpsServiceRoutineDefinition serviceRoutine);

    void authorisePersonSearch(TpsServiceRoutineDefinition serviceRoutine, String fnr);

    boolean isAuthorisedToUseServiceRutine(TpsServiceRoutineDefinition serviceRoutine);

    boolean isAuthorisedToFetchPersonInfo(TpsServiceRoutineDefinition serviceRoutine, String fnr);

}
