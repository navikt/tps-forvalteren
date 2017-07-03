package no.nav.tps.forvalteren.service.command.authorisation;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsMeldingDefinition;

public interface TpsAuthorisationService {

    void authoriseRestCall(TpsMeldingDefinition serviceRoutine);

    void authorisePersonSearch(TpsMeldingDefinition serviceRoutine, String fnr);

    boolean isAuthorisedToUseServiceRutine(TpsMeldingDefinition serviceRoutine);

    boolean isAuthorisedToFetchPersonInfo(TpsMeldingDefinition serviceRoutine, String fnr);

}
