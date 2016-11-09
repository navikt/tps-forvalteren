package no.nav.tps.vedlikehold.domain.service.command.tps;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests.TpsServiceRoutineRequest;

@Getter
@Setter
@NoArgsConstructor
public class Request {

    private String xml;
    private TpsServiceRoutineRequest routineRequest;
    private TpsRequestContext context;

}
