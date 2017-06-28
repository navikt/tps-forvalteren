package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import lombok.Getter;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

@Getter
@Setter
public class TpsMeldingDefinition {

    private String name;

    @JsonIgnore
    private TpsRequestConfig config;

    @JsonIgnore
    private List<ServiceRutineAuthorisationStrategy> requiredSecurityServiceStrategies;
}
