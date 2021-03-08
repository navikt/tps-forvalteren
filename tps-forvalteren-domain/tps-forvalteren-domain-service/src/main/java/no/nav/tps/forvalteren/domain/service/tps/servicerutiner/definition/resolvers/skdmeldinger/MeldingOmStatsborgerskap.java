package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import no.nav.tps.forvalteren.common.java.tpsapi.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.EndringAvStatsborgerskapSkdParametere;

public class MeldingOmStatsborgerskap implements SkdMeldingResolver {

    public static final String ENDRING_AV_STATSBORGERSKAP = "EndringAvStatsborgerskap";

    @Override
    public TpsSkdRequestMeldingDefinition resolve() {
        return TpsSkdMeldingDefinitionBuilder.aTpsSkdMelding()
                .name(ENDRING_AV_STATSBORGERSKAP)
                .config()
                .requestQueue(TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()
                .skdParameters()
                .addSkdParametersCreator(EndringAvStatsborgerskapSkdParametere.endringAvStatsborgerskapParameterCreator())
                .addParameterCreator()
                .and()
                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(WriteServiceRutineAuthorisation.writeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()
                .build();
    }
}
