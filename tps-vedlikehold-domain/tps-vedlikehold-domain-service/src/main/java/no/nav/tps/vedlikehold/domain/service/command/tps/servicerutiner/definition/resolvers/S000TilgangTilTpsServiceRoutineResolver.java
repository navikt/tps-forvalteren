package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.resolvers;

import static no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutineBuilder.aTpsServiceRoutine;

import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.definition.TpsServiceRoutine;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsPingRequest;

/**
 * @author Kenneth Gunnerud (Visma Consulting AS).
 */
public class S000TilgangTilTpsServiceRoutineResolver implements ServiceRoutineResolver {

    @Override
    public TpsServiceRoutine resolve() {
        return aTpsServiceRoutine()
                .name("FS03-OTILGANG-TILSRTPS-O")
                .internalName("S000 tilgangTilTps")
                .javaClass(TpsPingRequest.class)
                .build();
    }
}
