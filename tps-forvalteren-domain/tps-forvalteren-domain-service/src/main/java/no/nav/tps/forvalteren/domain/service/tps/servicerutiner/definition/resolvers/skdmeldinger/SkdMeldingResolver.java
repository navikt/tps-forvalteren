package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinition;

@FunctionalInterface
public interface SkdMeldingResolver {

    TpsSkdMeldingDefinition resolve();

}
