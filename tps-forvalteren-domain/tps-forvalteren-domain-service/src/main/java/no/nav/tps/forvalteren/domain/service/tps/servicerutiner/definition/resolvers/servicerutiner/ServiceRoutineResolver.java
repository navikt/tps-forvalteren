package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;


@FunctionalInterface
public interface ServiceRoutineResolver {
    TpsServiceRoutineDefinition resolve();
}