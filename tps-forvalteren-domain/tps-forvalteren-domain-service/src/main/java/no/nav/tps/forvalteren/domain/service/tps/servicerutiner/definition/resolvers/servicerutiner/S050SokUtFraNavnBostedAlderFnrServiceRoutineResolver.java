package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsSokPersonServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;
import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.RemoveUnauthorizedPeopleFromResponseTransform.removeUnauthorizedFnrFromResponse;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataListTransformer.extractDataListFromXml;
import static no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation;

public class S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutineDefinitionRequest resolve() {
        return TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine()
                .name("FS03-NAADRSOK-PERSDATA-O")
                .internalName("S050 Sok ut Fra navn")
                .javaClass(TpsSokPersonServiceRoutineRequest.class)
                .config()
                .requestQueue(REQUEST_QUEUE_SERVICE_RUTINE_ALIAS)

                .and()
                .parameter()
                .name("navn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("etternavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                .parameter()
                .name("fornavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("aksjonsKode")
                .required()
                .type(TpsParameterType.STRING)
                //.values("A0")
                .value("A2")
                .and()

                .parameter()
                .name("aksjonsDato")
                .optional()
                .type(TpsParameterType.DATE)
                .and()
                .parameter()
                .name("adresseNavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                .parameter()
                .name("postnr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                .parameter()
                .name("husnrFra")
                .optional()
                .type(TpsParameterType.STRING)
                .and()
                .parameter()
                .name("husnrTil")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("knr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("buffNr")
                .required()
                .values("1", "2", "3", "4", "5", "6")
                .type(TpsParameterType.STRING)
                .and()

                .transformer()
                .preSend(ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender())
                .postSend(removeUnauthorizedFnrFromResponse("antallTotalt", "antallFS050"))
                .postSend(extractDataListFromXml("personDataS050", "enPersonRes", "antallTotalt"))
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
