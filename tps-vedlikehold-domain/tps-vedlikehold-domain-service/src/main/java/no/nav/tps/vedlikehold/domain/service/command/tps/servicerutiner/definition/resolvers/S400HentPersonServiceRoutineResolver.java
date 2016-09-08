package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineParameter;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class S400HentPersonServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("FS03-FDNUMMER-PERSDATA-O")
                .internalName("S400 hentPerson")
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
