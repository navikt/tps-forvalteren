package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public interface ServiceRoutineResolver {

    TpsServiceRoutine resolve();
}