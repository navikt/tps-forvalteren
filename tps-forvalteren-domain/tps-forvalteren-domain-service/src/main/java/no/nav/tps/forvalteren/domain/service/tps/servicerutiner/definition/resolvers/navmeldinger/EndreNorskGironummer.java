package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.navmeldinger;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner.ServiceRoutineResolver;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreNorskGironummerRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

public class EndreNorskGironummer implements ServiceRoutineResolver {
    public static final String ENDRE_KONTONUMMER = "KontonummerEndringsmelding";

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name(ENDRE_KONTONUMMER)
                .internalName("Endre kontonummer")
                .javaClass(TpsEndreNorskGironummerRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .parameter()
                .name("offentligIdent")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("giroNrNorsk")
                .required()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("datoGiroNrNorsk")
                .optional()
                .type(TpsParameterType.DATE)
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
