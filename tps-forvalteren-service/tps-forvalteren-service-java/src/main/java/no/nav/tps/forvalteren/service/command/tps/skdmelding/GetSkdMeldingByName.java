package no.nav.tps.forvalteren.service.command.tps.skdmelding;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;

import java.util.Optional;

@FunctionalInterface
public interface GetSkdMeldingByName {

    Optional<TpsSkdRequestMeldingDefinition> execute(String skdMeldingName);

}
