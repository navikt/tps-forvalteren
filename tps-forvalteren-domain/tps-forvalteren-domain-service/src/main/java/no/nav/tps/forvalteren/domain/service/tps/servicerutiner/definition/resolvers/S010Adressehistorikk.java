package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentByFnrRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

public class S010Adressehistorikk implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-FDNUMMER-ADRHISTO-O")
                .internalName("Hent Adressehistorikk")
                .javaClass(TpsServiceRoutineHentByFnrRequest.class)
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
                .values("A0", "B0","C0", "C1")
                .and()

                .transformer()
                .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                .postSend(ResponseDataTransformer.extractDataFromXmlElement("personDataS010"))
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