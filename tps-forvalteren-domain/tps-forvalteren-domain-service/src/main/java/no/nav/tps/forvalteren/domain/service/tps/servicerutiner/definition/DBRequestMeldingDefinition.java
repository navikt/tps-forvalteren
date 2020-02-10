package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;

@Getter
@Setter
public class DBRequestMeldingDefinition {

    @JsonIgnore
    private List<ServiceRutineAuthorisationStrategy> requiredSecurityServiceStrategies;
}
