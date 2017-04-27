package no.nav.tps.forvalteren.domain.service.tps.servicerutiner.definition;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.nav.tps.forvalteren.domain.service.tps.authorisation.strategies.ServiceRutineAuthorisationStrategy;
import no.nav.tps.forvalteren.domain.service.tps.config.TpsRequestConfig;
import no.nav.tps.forvalteren.domain.service.tps.TpsParameter;
import no.nav.tps.forvalteren.domain.service.tps.servicerutiner.transformers.Transformer;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class TpsServiceRoutineDefinition {

    private String name;
    private String internalName;    // (DisplayName)

    @JsonIgnore
    private Class<?> javaClass;

    @JsonIgnore
    private TpsRequestConfig config;

    private List<TpsParameter> parameters;

    @JsonIgnore
    private List<Transformer> transformers;

    @JsonIgnore
    private List<ServiceRutineAuthorisationStrategy> requiredSecurityServiceStrategies;

}
