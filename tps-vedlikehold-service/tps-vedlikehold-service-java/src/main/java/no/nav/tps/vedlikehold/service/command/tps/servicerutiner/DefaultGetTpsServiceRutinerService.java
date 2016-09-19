package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import static java.util.stream.Collectors.toList;

import java.util.List;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers.ServiceRoutineResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Tobias Hansen (Visma Consulting AS).
 */
@Service
public class DefaultGetTpsServiceRutinerService implements GetTpsServiceRutinerService {

    @Autowired
    private List<ServiceRoutineResolver> resolvers;

    @Override
    public List<TpsServiceRoutine> exectue() {
        return resolvers.stream()
                .map(ServiceRoutineResolver::resolve)
                .collect(toList());
    }
}