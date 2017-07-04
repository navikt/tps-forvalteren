package no.nav.tps.forvalteren.service.command.tps.skdmelding;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger.SkdMeldingResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class DefaultGetTpsSkdmeldingService implements GetTpsSkdmeldingService{

    @Autowired(required = false)
    private List<SkdMeldingResolver> resolvers = new ArrayList<>();

    @Override
    public List<TpsSkdRequestMeldingDefinition> execute() {
        return resolvers.stream()
                .map(SkdMeldingResolver::resolve)
                .collect(toList());
    }
}
