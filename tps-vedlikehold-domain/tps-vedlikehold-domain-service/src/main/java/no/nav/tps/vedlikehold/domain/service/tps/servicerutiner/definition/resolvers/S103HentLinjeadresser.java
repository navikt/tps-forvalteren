package no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.requests.hent.TpsHentLinjeadresseServiceRoutineRequest;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.authorisation.strategies.ReadServiceRutineAuthorisation;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;

import static no.nav.tps.vedlikehold.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

public class S103HentLinjeadresser implements ServiceRoutineResolver {
    @Override
    public TpsServiceRoutineDefinition resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-FDNUMMER-ADRESSER-O")
                .internalName("S103 Hent Linjeadresser")
                .javaClass(TpsHentLinjeadresseServiceRoutineRequest.class)
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
                    .values("A0","A1","A2","A3", "B0","B1", "B2", "B3")
                .and()

                .parameter()
                    .name("adresseTypeS103")
                    .optional()
                    .type(TpsParameterType.STRING)
                    //TODO Flere hvis B(Tall) versjoner... "BOAD". Må gjøre noe med d.. Ikke med I A versjoner
                    .values("ALLE", "TIAD", "POST","UTAD","BOAD")
                .and()

                .parameter()
                    .name("aksjonsDato")
                    .optional()
                    .type(TpsParameterType.DATE)
                .and()


                .transformer()
                .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                .postSend(ResponseDataTransformer.extractDataFromXmlElement("personDataS103"))
                .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation())
                .addRequiredSearchAuthorisationStrategy(ReadServiceRutineAuthorisation.readAuthorisation())
                .addSecurity()

                .build();
    }
}
