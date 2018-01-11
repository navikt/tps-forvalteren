package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsHentPersonServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsSjekkTpsTilgjengeligServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

public class S000SjekkTpsTilgjengelig implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-OTILGANG-TILSRTPS-O")
                .internalName("Sjekk TPS er tilgjengelig")
                .javaClass(TpsSjekkTpsTilgjengeligServiceRoutineRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)
                .and()

                .transformer()
                .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addSecurity()

                .build();
    };
}
