package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.endring.TpsEndreNavnEndringsmeldingRequest;

import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_ENDRINGSMELDING_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.EndringsmeldingRequestTransform.endringsmeldingXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseDataTransformer.extractDataFromXmlElement;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

/**
 * Created by f148888 on 29.09.2016.
 */

public class EndreNavn implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
                .name("EndreNavn")
                .internalName("Endre: Navn")
                .javaClass(TpsEndreNavnEndringsmeldingRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_ENDRINGSMELDING_ALIAS)
                .and()

                .parameter()
                    .name("offentligIdent")
                    .required()
                    .type(TpsParameterType.STRING)

                .and()
                .transformer()
                    .preSend(endringsmeldingXmlWrappingAppender())
                    .postSend(extractDataFromXmlElement("endreNavn"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .parameter()
                    .name("fornavn")
                    .required()
                    .type(TpsParameterType.STRING)

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
                .build();
    }
}
