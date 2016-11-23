package no.nav.tps.vedlikehold.domain.service.command.tps.servicerutiner.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Øyvind Grimnes, Visma Consulting AS
 */

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TpsServiceRoutineRequest {

    private String serviceRutinenavn;

}

