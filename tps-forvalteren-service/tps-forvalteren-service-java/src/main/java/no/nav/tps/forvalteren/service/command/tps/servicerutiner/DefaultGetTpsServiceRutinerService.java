package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DefaultGetTpsServiceRutinerService implements GetTpsServiceRutinerService {

    @Autowired(required = false)
    private List<ServiceRoutineResolver> resolvers = new ArrayList<>();

    @Override
    public List<TpsServiceRoutineDefinition> execute() {
        return resolvers.stream()
                .map(ServiceRoutineResolver::resolve)
                .collect(toList());
    }
}