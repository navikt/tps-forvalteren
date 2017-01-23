package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.hent.TpsHentFnrHistMultiServiceRoutineRequest;

import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseDataListTransformer.extractDataListFromXml;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.RemoveTakenFnrFromResponseTransform.removeTakenFnrFromResponseTransform;

/**
 * Created by Peter Fløgstad on 17.01.2017.
 */
public class M201HentFnrNavnDiskresjonPaFlerePersoner implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
                .name("FS03-FDLISTER-DISKNAVN-M")
                .internalName("M201 Hent Fnr Navn")
                .javaClass(TpsHentFnrHistMultiServiceRoutineRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)

                .and()
                .parameter()
                    .name("antallFnr")
                    .required()
                    .type(TpsParameterType.STRING)

                //TODO Trenger kanskje ikke buff på denne.
                .and()
                .parameter()
                    .name("buffNr")
                    .required()
                    .type(TpsParameterType.STRING)

                .and()
                .parameter()
                    .name("aksjonsKode")
                    .required()
                    .type(TpsParameterType.STRING)
                    .values("A0")

                .and()
                .parameter()
                    .name("aksjonsDato")
                    .optional()
                    .type(TpsParameterType.DATE)

                .and()
                .transformer()
                    .preSend(serviceRoutineXmlWrappingAppender())
                    .postSend(removeTakenFnrFromResponseTransform("antallFM201"))
                    .postSend(extractDataListFromXml("personDataM201", "EFnr", "antallFM201"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .build();
    }
}
