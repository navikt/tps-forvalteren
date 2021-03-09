package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import no.nav.tps.forvalteren.common.tpsapi.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.AnnenAvgangSkdParametere;

public class MeldingOmAnnenAvgang implements SkdMeldingResolver {

    public static final String MELDING_OM_ANNEN_AVGANG = "MeldingOmAnnenAvgang" ;

    @Override
    public TpsSkdRequestMeldingDefinition resolve() {
        return TpsSkdMeldingDefinitionBuilder.aTpsSkdMelding()
                .name(MELDING_OM_ANNEN_AVGANG)
                .config()
                .requestQueue(TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()
                .skdParameters()
                .addSkdParametersCreator(AnnenAvgangSkdParametere.annenAvgangParameterCreator())
                .addParameterCreator()
                .and()
                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(WriteServiceRutineAuthorisation.writeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()
                .build();
    }
}
