package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.skdmeldinger;

import no.nav.tps.forvalteren.common.java.util.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.WriteServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdMeldingDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsSkdRequestMeldingDefinition;
import no.nav.tps.forvalteren.domain.service.tps.skdmelding.parameters.UtvandringSkdParametere;

public class UtvandringAarsakskode32 implements SkdMeldingResolver{
    public static final String UTVANDRING_INNVANDRING_MLD_NAVN = "Utvandring/Innvandring-melding";
    @Override
    public TpsSkdRequestMeldingDefinition resolve(){
        return TpsSkdMeldingDefinitionBuilder.aTpsSkdMelding()
                .name(UTVANDRING_INNVANDRING_MLD_NAVN)
                .config()
                .requestQueue(TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()
                .skdParameters()
                .addSkdParametersCreator(UtvandringSkdParametere.utvandringsParameterCreator())
                .addParameterCreator()
                .and()
                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(WriteServiceRutineAuthorisation.writeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()
                .build();
    }
}
