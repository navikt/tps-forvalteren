package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsHentFnrHistMultiServiceRoutineRequest;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.RemoveTakenFnrFromResponseTransform.removeTakenFnrFromResponseTransform;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataListTransformer.extractDataListFromXml;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

public class M201HentFnrNavnDiskresjonPaFlerePersoner implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
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

                .and()
                .parameter()
                .name("aksjonsKode")
                .required()
                .type(TpsParameterType.STRING)
                .values("A0")

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
