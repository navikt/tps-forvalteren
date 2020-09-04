package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineHentByFnrRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

public class S610HentGT implements ServiceRoutineResolver{
    public static final String PERSON_KERNINFO_SERVICE_ROUTINE = "FS03-FDNUMMER-KERNINFO-O";

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name(PERSON_KERNINFO_SERVICE_ROUTINE)
                .internalName("Hent kjerneinfo")
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
                    .values("A0", "B0", "B1", "C0", "C1", "D0", "D1")
                .and()

                .transformer()
                    .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                    .postSend(ResponseDataTransformer.extractDataFromXmlElement("person"))
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
