package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.resolvers.servicerutiner;

import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition.TpsServiceRoutineDefinitionBuilder;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.hent.TpsSokPersonServiceRoutineRequest;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataTransformer;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseStatusTransformer;
import no.nav.tps.forvalteren.domain.service.tps.TpsParameterType;

import static no.nav.tps.forvalteren.domain.service.tps.config.TpsConstants.REQUEST_QUEUE_SERVICE_RUTINE_ALIAS;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.RemoveUnauthorizedPeopleFromResponseTransform.removeUnauthorizedFnrFromResponse;
import static no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.response.ResponseDataListTransformer.extractDataListFromXml;
import static no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.DiskresjonskodeServiceRutineAuthorisation.diskresjonskodeAuthorisation;
import static no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.EgenAnsattServiceRutineAuthorisation.egenAnsattAuthorisation;

public class S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver implements ServiceRoutineResolver { //NOSONAR

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
                .name("navnFTE")
                .optional()
                .type(TpsParameterType.STRING)
                .values("F","T","E")
                .and()

                .parameter()
                .name("navnehist")
                .optional()
                .type(TpsParameterType.STRING)
                .values("J","N")
                .and()

                .parameter()
                .name("etternavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("etternavnFTE")
                .optional()
                .type(TpsParameterType.STRING)
                .values("F","T","E")
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
                .value("A0")
                .and()

                .parameter()
                .name("aksjonsDato")
                .optional()
                .type(TpsParameterType.DATE)
                .and()

                .parameter()
                .name("fodselsdatofra")
                .optional()
                .type(TpsParameterType.DATE)
                .and()

                .parameter()
                .name("fodselsdatotil")
                .optional()
                .type(TpsParameterType.DATE)
                .and()

                .parameter()
                .name("alderfra")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("aldertil")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("kjonn")
                .optional()
                .type(TpsParameterType.STRING)
                .values("K","M")
                .and()

                .parameter()
                .name("identType")
                .optional()
                .type(TpsParameterType.STRING)
                .values("FNR","DNR")
                .and()

                .parameter()
                .name("personStatus")
                .optional()
                .type(TpsParameterType.STRING)
                .values("BOSA","UTVA", "DØD", "LEV")
                .and()

                .parameter()
                .name("statsborgerskap")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("adresseType")
                .optional()
                .type(TpsParameterType.STRING)
                .values("BOAD","POST", "TIAD", "UTAD")
                .and()

                .parameter()
                .name("adressehist")
                .optional()
                .type(TpsParameterType.STRING)
                .values("J","N")
                .and()

                .parameter()
                .name("adresseNavn")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("adresseFTE")
                .optional()
                .type(TpsParameterType.STRING)
                .values("F","T","E")
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
                .name("husbokstavfra")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("husnrTil")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("husbokstavtil")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("knr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("landKode")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("tknr")
                .optional()
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("sortering")
                .required()
                .values("Navn", "Adresse", "Fnr")
                .type(TpsParameterType.STRING)
                .and()

                .parameter()
                .name("stigAvt")
                .optional()
                .type(TpsParameterType.STRING)
                .values("S","A")
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
                .postSend(ResponseDataTransformer.extractDataFromXmlElement("personDataS050"))
                //.postSend(extractDataListFromXml("personDataS050", "enPersonRes", "antallTotalt"))
                .postSend(ResponseStatusTransformer.extractStatusFromXmlElement("svarStatus"))
                .and()

                .securityBuilder()
                .addRequiredSearchAuthorisationStrategy(diskresjonskodeAuthorisation())
                .addRequiredSearchAuthorisationStrategy(egenAnsattAuthorisation())
                .addSecurity()

                .build();
    }
}