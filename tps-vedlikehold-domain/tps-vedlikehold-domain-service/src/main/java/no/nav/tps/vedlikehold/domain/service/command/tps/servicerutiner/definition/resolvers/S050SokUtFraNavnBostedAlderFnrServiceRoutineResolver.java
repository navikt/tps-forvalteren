package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinition;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.hent.TpsSokPersonServiceRoutineRequest;

import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.DiskresjonskodeAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.authorisation.strategies.EgenAnsattAuthorisation.egenAnsattAuthorisation;
import static no.nav.tps.vedlikehold.domain.service.command.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.RemoveUnauthorizedPeopleFromResponseTransform.removeUnauthorizedFnrFromResponse;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseDataListTransformer.extractDataListFromXml;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.response.ResponseStatusTransformer.extractStatusFromXmlElement;

/**
 * Created by f148888 on 30.09.2016.
 */
public class S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutineDefinition resolve() {
        return aTpsServiceRoutine()
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
                    .values("A0")
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
                    .values("1","2","3","4","5","6")
                    .type(TpsParameterType.STRING)
                .and()
                .transformer()
                    .preSend(serviceRoutineXmlWrappingAppender())
                    .postSend(removeUnauthorizedFnrFromResponse("antallTotalt", "antallFS050"))
                    .postSend(extractDataListFromXml("personDataS050", "enPersonRes", "antallTotalt"))
                    .postSend(extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                    .addRequiredSearchAuthorisationStrategy(diskresjonskodeAuthorisation())
                    .addRequiredSearchAuthorisationStrategy(egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}
