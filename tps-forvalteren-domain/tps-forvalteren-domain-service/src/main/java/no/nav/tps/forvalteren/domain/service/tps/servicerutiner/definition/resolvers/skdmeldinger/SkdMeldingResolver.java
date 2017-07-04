package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;

@FunctionalInterface
public interface SkdMeldingResolver {

    TpsSkdRequestMeldingDefinition resolve();

}
