package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.endring.TpsEndreNavnEndringsmeldingRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;
import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;


public class EndreNavn implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("EndreNavn")
                .internalName("Endre: Navn")
                .javaClass(TpsEndreNavnEndringsmeldingRequest.class)
                .config()
                    .requestQueue(TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .parameter()
                .name("fornavn")
                .required()
                .type(TpsParameterType.STRING)

                .and()

                .parameter()
                    .name("offentligIdent")
                    .required()
                    .type(TpsParameterType.STRING)

                .and()
                .transformer()
                    .preSend(EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender())
                    .postSend(ResponseDataTransformer.extractDataFromXmlElement("endreNavn"))
                    .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .parameter()
                    .name("mellomnavn")
                    .required()
                    .type(TpsParameterType.STRING)

                .and()
                    .parameter()
                    .name("etternavn")
                    .required()
                    .type(TpsParameterType.STRING)

                .and()
                    .parameter()
                    .name("kortnavn")
                    .required()
                .type(TpsParameterType.STRING)

                .and()
                .parameter()
                    .name("tidligerenavn")
                    .required()
                    .type(TpsParameterType.STRING)

                .and()
                .parameter()
                    .name("datoNyttNavn")
                    .required()
                    .type(TpsParameterType.DATE)

                .and()
                .parameter()
                    .name("kilde")
                    .required()
                    .type(TpsParameterType.STRING)
                    .values("FS22")

                .and()
                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
