package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;


import no.nav.tps.vedlikehold.domain.service.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.hent.TpsHentKontaktinformasjonServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;

import static no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

public class S600HentKontaktinformasjonServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-FDNUMMER-KONTINFO-O")
                .internalName("S600 Hent Kontaktinformasjon")
                .javaClass(TpsHentKontaktinformasjonServiceRoutineRequest.class)
                .config()
                    .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)
                .and()
                .parameter()
                    .name("fnr")
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
                    .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                    .postSend(ResponseDataTransformer.extractDataFromXmlElement("person"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
