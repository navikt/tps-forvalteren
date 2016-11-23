package no.nav.tps.vedlikehold.service.command.tps.servicerutiner;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.service.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindServiceRoutineByName implements Command {

    @Autowired
    private GetTpsServiceRutinerService getTpsServiceRutinerService;

    public Optional<TpsServiceRoutineDefinition> execute(String serviceName) {
        return getTpsServiceRutinerService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(serviceName))
                .findFirst();
    }

}
