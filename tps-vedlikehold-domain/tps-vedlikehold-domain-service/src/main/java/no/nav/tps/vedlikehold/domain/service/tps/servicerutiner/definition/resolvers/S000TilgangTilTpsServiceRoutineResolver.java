package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattAuthorisation;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;

import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.hent.TpsPingServiceRoutineRequest;

public class S000TilgangTilTpsServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-OTILGANG-TILSRTPS-O")
                .internalName("S000 Tilgang til Tps")
                .javaClass(TpsPingServiceRoutineRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)
                .and()
                .transformer()
                    .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                    .addRequiredSearchAuthorisationStrategy(DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation())
                    .addRequiredSearchAuthorisationStrategy(EgenAnsattAuthorisation.egenAnsattAuthorisation())
                    .addSecurity()

                .build();
    }
}
