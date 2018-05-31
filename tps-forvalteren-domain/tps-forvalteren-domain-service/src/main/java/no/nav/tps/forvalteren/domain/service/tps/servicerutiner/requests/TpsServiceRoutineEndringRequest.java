package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TpsServiceRoutineEndringRequest extends TpsServiceRoutineRequest {
    private String offentligIdent;
}
