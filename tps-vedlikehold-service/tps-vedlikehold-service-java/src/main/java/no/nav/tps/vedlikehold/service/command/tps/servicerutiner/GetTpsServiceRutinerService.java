package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import java.util.List;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;


@FunctionalInterface
public interface GetTpsServiceRutinerService {
    List<TpsServiceRoutineDefinition> execute();
}