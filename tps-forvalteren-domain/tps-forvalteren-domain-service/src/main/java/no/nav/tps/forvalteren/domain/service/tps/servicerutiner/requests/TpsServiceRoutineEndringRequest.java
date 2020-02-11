package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TpsServiceRoutineEndringRequest extends TpsServiceRoutineRequest {
    private String offentligIdent;
    
    public TpsServiceRoutineEndringRequest(String serviceRutinenavn, String offentligIdent) {
        super(serviceRutinenavn);
        this.offentligIdent = offentligIdent;
    }
}
