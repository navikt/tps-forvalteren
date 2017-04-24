package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsServiceRoutineRequest {

    private String serviceRutinenavn;

}

