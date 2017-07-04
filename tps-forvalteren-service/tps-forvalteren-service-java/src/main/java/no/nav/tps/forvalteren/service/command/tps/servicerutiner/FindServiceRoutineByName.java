package no.nav.tps.forvalteren.service.command.tps.servicerutiner;

import no.nav.tps.forvalteren.service.command.Command;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindServiceRoutineByName implements Command {

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    public Optional<TpsServiceRoutineDefinitionRequest> execute(String serviceName) {
        return getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceName))
                .findFirst();
    }

}
