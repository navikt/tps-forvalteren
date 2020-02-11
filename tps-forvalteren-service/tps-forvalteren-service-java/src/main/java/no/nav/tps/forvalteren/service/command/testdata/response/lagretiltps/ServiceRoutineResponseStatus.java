package no.nav.tps.forvalteren.service.command.testdata.response.lagretiltps;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRoutineResponseStatus {

    private String personId;
    private String serviceRutinenavn;
    private Map<String, String> status;

    public Map getStatus() {
        if(isNull(status)) {
            status = new HashMap<>();
        }
        return status;
    }
}