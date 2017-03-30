package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;


@FunctionalInterface
public interface ServiceRoutineResolver {
    TpsServiceRoutineDefinition resolve();
}