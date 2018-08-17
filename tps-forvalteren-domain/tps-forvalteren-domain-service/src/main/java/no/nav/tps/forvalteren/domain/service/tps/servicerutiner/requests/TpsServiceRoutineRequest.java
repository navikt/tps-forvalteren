package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.requests;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsServiceRoutineRequest {
    
    private String serviceRutinenavn;
    
}

