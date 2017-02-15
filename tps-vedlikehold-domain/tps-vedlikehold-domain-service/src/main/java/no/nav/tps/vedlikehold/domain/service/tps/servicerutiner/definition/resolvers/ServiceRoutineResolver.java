package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
@FunctionalInterface
public interface ServiceRoutineResolver {
    TpsServiceRoutineDefinition resolve();
}