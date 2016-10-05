package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import no.nav.tps.vedlikehold.domain.service.command.tps.TpsParameterType;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsSokPersonRequestServiceRoutine;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;

/**
 * Created by f148888 on 30.09.2016.
 */
public class S050SokPaNavnServiceRoutineResolver implements ServiceRoutineResolver{

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("FS03-NAADRSOK-PERSDATA-O")
                .internalName("S050 Sok Person")
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
                .build();
    }
}
