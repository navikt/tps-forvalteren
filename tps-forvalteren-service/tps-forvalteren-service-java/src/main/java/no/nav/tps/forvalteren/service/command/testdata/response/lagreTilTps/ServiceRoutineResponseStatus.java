package no.nav.tps.forvalteren.service.command.testdata.response.lagreTilTps;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.ResponseStatus;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRoutineResponseStatus {
    private String personId;
    private String serviceRutinenavn;
    private String environment;
    private ResponseStatus status;
    
}
