package no.nav.tps.forvalteren.service.command.testdata.skd;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;

import java.util.Set;

@FunctionalInterface
public interface SendSkdMeldingTilGitteMiljoer {

    void execute(String skdMelding, TpsSkdRequestMeldingDefinition skdRequestMeldingDefinition, Set<String> environments);

}
