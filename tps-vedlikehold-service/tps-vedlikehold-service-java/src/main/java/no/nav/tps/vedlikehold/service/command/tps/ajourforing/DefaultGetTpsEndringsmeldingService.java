package no.nav.tps.vedlikehold.service.command.tps.ajourforing;

import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.TpsEndringsmelding;
import no.nav.tps.vedlikehold.domain.service.command.tps.ajourforing.definition.resolvers.EndringsmeldingResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by f148888 on 30.09.2016.
 */
public class DefaultGetTpsEndringsmeldingService implements GetTpsEndringsmeldingerService{

    @Autowired
    private List<EndringsmeldingResolver> endringsmeldingResolvers;

    @Override
    public List<TpsEndringsmelding> execute() {
        return endringsmeldingResolvers
                .stream()
                .map(EndringsmeldingResolver::resolve)
                .collect(Collectors.toList());
    }

}
