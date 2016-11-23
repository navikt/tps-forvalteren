package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@FunctionalInterface
public interface GetTpsServiceRutinerService {
    List<TpsServiceRoutineDefinition> execute();
}