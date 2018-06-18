package no.nav.tps.forvalteren.service.command.testdata.skd;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests.TpsServiceRoutineRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TpsNavEndringsMelding {

    private TpsServiceRoutineRequest melding;

    private String miljo;

}
