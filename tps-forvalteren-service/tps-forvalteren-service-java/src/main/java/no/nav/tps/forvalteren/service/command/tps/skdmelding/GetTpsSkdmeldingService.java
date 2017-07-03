package no.nav.tps.forvalteren.service.command.tps.skdmelding;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinition;

import java.util.List;

@FunctionalInterface
public interface GetTpsSkdmeldingService {
    List<TpsSkdMeldingDefinition> execute();
}
