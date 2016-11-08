package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsSokPersonRequestServiceRoutine;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;
import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.transformers.request.ServiceRoutineRequestTransform.serviceRoutineXmlWrappingAppender;

/**
 * Created by f148888 on 30.09.2016.
 */
public class S050SokUtFraNavnBostedAlderFnrServiceRoutineResolver implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("FS03-NAADRSOK-PERSDATA-O")
                .internalName("S050 Sok ut Fra navn")
                .javaClass(TpsSokPersonRequestServiceRoutine.class)
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
                    .values("1","2","3","4","5", "6")
                    .type(TpsParameterType.STRING)
                .and()
                .transformer()
                  .preSend(serviceRoutineXmlWrappingAppender())
                .and()
                .build();
    }
}
