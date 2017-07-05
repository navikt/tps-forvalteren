package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;


@FunctionalInterface
public interface ServiceRoutineResolver {
    TpsServiceRoutineDefinitionRequest resolve();
}