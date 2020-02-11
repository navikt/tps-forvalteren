package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TpsServiceRoutineResponse {

    private String xml;
    private Object response;

}
