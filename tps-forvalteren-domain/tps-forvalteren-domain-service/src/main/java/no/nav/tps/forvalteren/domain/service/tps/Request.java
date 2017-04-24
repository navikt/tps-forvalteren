package no.nav.tps.forvalteren.domain.service.tps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsRequestContext;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    private String xml;
    private TpsServiceRoutineRequest routineRequest;
    private TpsRequestContext context;

}
