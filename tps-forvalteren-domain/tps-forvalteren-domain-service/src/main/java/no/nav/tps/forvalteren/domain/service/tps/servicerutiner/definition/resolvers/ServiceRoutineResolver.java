package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;


@FunctionalInterface
public interface ServiceRoutineResolver {
    TpsServiceRoutineDefinition resolve();
}