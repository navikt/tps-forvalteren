package no.nav.tps.forvalteren.service.command.authorisation;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.DBRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsRequestMeldingDefinition;

public interface DBAuthorisationService {

    void authoriseRestCall(DBRequestMeldingDefinition serviceRoutine);

    void authorisePersonSearch(DBRequestMeldingDefinition serviceRoutine, String fnr);

    boolean isAuthorisedToUseServiceRutine(TpsRequestMeldingDefinition serviceRoutine);

    boolean isAuthorisedToFetchPersonInfo(TpsRequestMeldingDefinition serviceRoutine, String fnr);

}
