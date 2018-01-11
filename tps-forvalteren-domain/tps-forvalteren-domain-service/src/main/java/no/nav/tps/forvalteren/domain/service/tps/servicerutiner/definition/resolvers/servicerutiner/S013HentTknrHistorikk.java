package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsHentTknrHistorikkServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;

public class S013HentTknrHistorikk implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-FDNUMMER-TKNRHIST-O")
                .internalName("Hent Tknr historikk")
                .javaClass(TpsHentTknrHistorikkServiceRoutineRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)
                .and()
                .parameter()
                .name("fnr")
                .required()
                .type(TpsParameterType.STRING)
                .and()
                .parameter()
                .name("aksjonsDato")
                .optional()
                .type(TpsParameterType.DATE)
                .and()
                .parameter()
                .name("aksjonsKode")
                .required()
                .type(TpsParameterType.STRING)
                .value("A0")
                .and()

                .transformer()
                .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                .postSend(ResponseDataTransformer.extractDataFromXmlElement("personDataS013"))
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy((EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation()))
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()

                .build();

    }
}