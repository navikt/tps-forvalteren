package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import java.util.List;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;


@FunctionalInterface
public interface GetTpsServiceRutinerService {
    List<TpsServiceRoutineDefinitionRequest> execute();
}