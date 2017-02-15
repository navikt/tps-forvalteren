package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.endring.TpsEndreNavnEndringsmeldingRequest;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;

import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

/**
 * Created by f148888 on 29.09.2016.
 */

public class EndreNavn implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("EndreNavn")
                .internalName("Endre: Navn")
                .javaClass(TpsEndreNavnEndringsmeldingRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
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
                    .preSend(endringsmeldingXmlWrappingAppender())
                    .postSend(ResponseDataTransformer.extractDataFromXmlElement("endreNavn"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
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
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(EgenAnsattAuthorisation.egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
