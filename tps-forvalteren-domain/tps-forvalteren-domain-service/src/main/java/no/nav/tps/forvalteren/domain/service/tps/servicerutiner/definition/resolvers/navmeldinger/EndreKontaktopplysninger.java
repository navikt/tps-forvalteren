package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger;

import static no.nav.tps.forvalteren.common.java.tpsapi.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreKontaktopplysningerRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

public class EndreKontaktopplysninger implements ServiceRoutineResolver {
    public static final String ENDRE_KONTAKTOPPLYSNINGER = "KontaktopplysningEndringer";

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name(ENDRE_KONTAKTOPPLYSNINGER)
                .internalName("Endre kontaktopplysninger")
                .javaClass(TpsEndreKontaktopplysningerRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .transformer()
                .preSend(EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender())
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()

                .build();
    }
}
