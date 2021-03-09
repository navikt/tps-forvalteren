package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import org.springframework.stereotype.Service;

import no.nav.tps.forvalteren.common.tpsapi.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.BarnForeldrerelasjonSkdParametre;

@Service
public class BarnForeldrerelasjonEndringAarsakskode98 implements SkdMeldingResolver {

    public static final String BARN_FORELDRE_RELASJON_ENDRING = "BarnForeldreRelasjonEndring";

    @Override
    public TpsSkdRequestMeldingDefinition resolve() {
        return TpsSkdMeldingDefinitionBuilder.aTpsSkdMelding()
                .name(BARN_FORELDRE_RELASJON_ENDRING)

                .config()
                .requestQueue(TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .skdParameters()
                .addSkdParametersCreator(BarnForeldrerelasjonSkdParametre.barnForeldrerelasjonSkdParametreCreator())
                .addParameterCreator()

                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(WriteServiceRutineAuthorisation.writeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()

                .build();
    }
}
