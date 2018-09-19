package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.FamilieendringSkdParamtere;

@Service
public class Familieendring implements SkdMeldingResolver {

    public static final String FAMILIEENDRING_MLD_NAVN = "Familieendring";

    @Override
    public TpsSkdRequestMeldingDefinition resolve() {
        return TpsSkdMeldingDefinitionBuilder.aTpsSkdMelding()
                .name(FAMILIEENDRING_MLD_NAVN)

                .config()
                .requestQueue(TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .skdParameters()
                .addSkdParametersCreator(FamilieendringSkdParamtere.familieendringSkdParamterCreator())
                .addParameterCreator()

                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(WriteServiceRutineAuthorisation.writeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()

                .build();
    }
}
