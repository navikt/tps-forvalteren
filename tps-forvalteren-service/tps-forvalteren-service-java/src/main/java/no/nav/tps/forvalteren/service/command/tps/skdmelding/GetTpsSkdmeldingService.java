package no.nav.tps.forvalteren.service.command.tps.skdmelding;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;

import java.util.List;

@FunctionalInterface
public interface GetTpsSkdmeldingService {
    List<TpsSkdRequestMeldingDefinition> execute();
}
