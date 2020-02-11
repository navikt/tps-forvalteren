package no.nav.tps.forvalteren.service.command.tps.skdmelding;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetSkdMeldingByName {

    @Autowired
    private GetTpsSkdmeldingService getTpsSkdmeldingService;

    public Optional<TpsSkdRequestMeldingDefinition> execute(String skdMeldingName) {
        return getTpsSkdmeldingService.execute()
                .stream()
                .filter(request -> request.getName().equalsIgnoreCase(skdMeldingName))
                .findFirst();
    }
}
