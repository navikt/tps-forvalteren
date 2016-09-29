package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

/**
 * Created by f148888 on 26.09.2016.
 */

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineParameter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsHentKontaktinformasjonServiceRoutine;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;


public class S600HentKontaktinformasjon implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("FS03-FDNUMMER-KONTINFO-O")
                .internalName("S600 Hent Kontaktinformasjon")
                .javaClass(TpsHentKontaktinformasjonServiceRoutine.class)
                .parameter()
                    .name("fnr")
                    .required()
                    .type(TpsServiceRoutineParameter.Type.STRING)
                .and()
                .parameter()
                    .name("aksjonsKode")
                    .required()
                    .type(TpsServiceRoutineParameter.Type.STRING)
                    .values("E0", "A0", "A2", "B0", "B2", "C0", "D0")
                .and()
                .parameter()
                    .name("aksjonsDato")
                    .optional()
                    .type(TpsServiceRoutineParameter.Type.DATE)
                .and()
                .build();
    }
}
